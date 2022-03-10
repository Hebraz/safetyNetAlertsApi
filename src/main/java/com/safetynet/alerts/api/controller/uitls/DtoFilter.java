package com.safetynet.alerts.api.controller.uitls;

import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.http.converter.json.MappingJacksonValue;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DtoFilter {
    public static <T> MappingJacksonValue apply(T dtoToFilter,  Map<String, Set<String>> filters ){
        SimpleFilterProvider filterList = new SimpleFilterProvider();
        for(String filterName : filters.keySet()){
            SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept(filters.get(filterName));
            filterList.addFilter(filterName, filter);
        }
        MappingJacksonValue filtererDto = new MappingJacksonValue(dtoToFilter);
        filtererDto.setFilters(filterList);
        return filtererDto;
    }
}
