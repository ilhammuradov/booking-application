package az.edu.turing.dao;

import az.edu.turing.dao.entity.FlightsEntity;

public abstract class FlightsRepository implements Repository<FlightsEntity> {
    public abstract void update(FlightsEntity flightsEntity);
}
