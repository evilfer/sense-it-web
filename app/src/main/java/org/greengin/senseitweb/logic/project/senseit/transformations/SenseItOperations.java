package org.greengin.senseitweb.logic.project.senseit.transformations;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.greengin.senseitweb.entities.activities.senseit.SenseItActivity;
import org.greengin.senseitweb.entities.activities.senseit.SenseItTransformation;
import org.greengin.senseitweb.entities.activities.senseit.SensorInput;
import org.greengin.senseitweb.logic.project.senseit.transformations.maths.Abs;
import org.greengin.senseitweb.logic.project.senseit.transformations.maths.Derivative;
import org.greengin.senseitweb.logic.project.senseit.transformations.maths.FilterCC;
import org.greengin.senseitweb.logic.project.senseit.transformations.maths.GetX;
import org.greengin.senseitweb.logic.project.senseit.transformations.maths.GetY;
import org.greengin.senseitweb.logic.project.senseit.transformations.maths.GetZ;
import org.greengin.senseitweb.logic.project.senseit.transformations.maths.Integrate;
import org.greengin.senseitweb.logic.project.senseit.transformations.maths.Max;
import org.greengin.senseitweb.logic.project.senseit.transformations.maths.Min;
import org.greengin.senseitweb.logic.project.senseit.transformations.maths.Modulus;
import org.greengin.senseitweb.utils.TimeValue;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class SenseItOperations implements InitializingBean {

    private static SenseItOperations instance;

    private static final HashMap<String, SenseItOperation> ops;
	static {
		ops = new HashMap<String, SenseItOperation>();
		ops.put("integrate", new Integrate());
		ops.put("derivative", new Derivative());
		ops.put("modulus", new Modulus());
		ops.put("abs", new Abs());
		ops.put("getx", new GetX());
		ops.put("gety", new GetY());
		ops.put("getz", new GetZ());
		ops.put("removecc", new FilterCC());
		ops.put("max", new Max());
		ops.put("min", new Min());
	}

    @Autowired
    private ResourceLoader resourceLoader;

	private SenseItData data = null;

    public static SenseItOperations instance() {
        return instance;
    }

    @Override
    public void afterPropertiesSet() {
        init();
        instance = this;
    }


	public void init() {
		if (data == null) {
			data = loadData();
		}
	}

	private SenseItData loadData() {
		ObjectMapper mapper = new ObjectMapper();
		try {
            Resource resource = resourceLoader.getResource("WEB-INF/senseit/data.json");
			return mapper.readValue(resource.getInputStream(), SenseItData.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public SenseItProcessedSeries process(HashMap<Long, Vector<TimeValue>> series, SenseItActivity activity) {
		int index = 0;
		SenseItProcessedSeries processed = new SenseItProcessedSeries();

        for (SensorInput input : activity.getProfile().getSensorInputs()) {
            SenseItDataSensor sensorData = data.sensorTypes.get(input.getSensor());
            if (sensorData != null) {
                String label = String.format("%s raw data", sensorData.name);
                SenseItProcessedSeriesVariable var = new SenseItProcessedSeriesVariable(index++, label, series.get(input.getId()));
                var.units.apply(sensorData.units);
                processed.values.put(String.valueOf(input.getId()), var);
            }
        }


		boolean goon = true;
		while (goon) {
			goon = false;
			for (SenseItTransformation tx : activity.getProfile().getTx()) {
				String varId = tx.getId();
				if (processed.values.get(varId) == null) {
					SenseItOperation op = ops.get(tx.getType());
					if (op != null) {
						SenseItProcessedSeriesVariable input = null;
						Vector<Vector<TimeValue>> inputs = new Vector<Vector<TimeValue>>();
						boolean good = true;
						for (String inputId : tx.getInputs()) {
							input = processed.values.get(inputId);
							if (input == null) {
								good = false;
								break;
							} else {
								inputs.add(input.values);
							}
						}

						if (good) {
							SenseItProcessedSeriesVariable result = new SenseItProcessedSeriesVariable(index++, tx.getName());
							if (op.process(inputs, result.values)) {
								SenseItDataTransformation txData = data.transformations.get(tx.getType());
								if (input != null) {
									result.units.apply(input.units);
								}
								result.units.apply(txData.getUnits());
								processed.values.put(varId, result);
								goon = true;
							}
						}
					}
				}
			}
		}

		return processed;
	}

	public static HashMap<String, TimeValue> tableVariables(SenseItProcessedSeries data, SenseItActivity activity) {
		HashMap<String, TimeValue> values = new HashMap<String, TimeValue>();
		for (SenseItTransformation tx : activity.getProfile().getTx()) {
			String type = tx.getType();
			if ("max".equals(type) || "min".equals(type)) {
				String id = tx.getId();
				SenseItProcessedSeriesVariable var = data.values.get(id);
				values.put(id, var != null && var.values.size() > 0 ? var.values.firstElement() : null);
			}
		}

		return values;
	}

}
