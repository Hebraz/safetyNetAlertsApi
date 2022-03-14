package com.safetynet.alerts.api.service.dtomapper;

/**
 * Entity object to corresponding dto object Mapper
 * @param <T> entity class
 * @param <U> dto class
 */
public interface IDtoMapper<T,U> {
    /**
     * Map an entity object to its corresponding dto object
     * @param entityToMap entity object
     * @return dto object
     */
    public U mapToDto(T entityToMap);
}
