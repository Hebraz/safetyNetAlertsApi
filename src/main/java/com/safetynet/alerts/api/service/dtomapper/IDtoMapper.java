package com.safetynet.alerts.api.service.dtomapper;

public interface IDtoMapper<T,U> {
    public U mapToDto(T entityToMap);
}
