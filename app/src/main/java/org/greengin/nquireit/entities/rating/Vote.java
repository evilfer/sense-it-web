package org.greengin.nquireit.entities.rating;


import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;
import org.greengin.nquireit.entities.AbstractEntity;
import org.greengin.nquireit.entities.users.UserProfile;


@Entity
public class Vote extends AbstractEntity {	

	@Basic
    @Getter
    @Setter
    Long value;

	@ManyToOne
    @Getter
    @Setter
    UserProfile user;

	@ManyToOne
    @Getter
    @Setter
	VotableEntity target;

}
