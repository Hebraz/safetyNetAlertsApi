package com.safetynet.alerts.api.service.dtomapper;

import com.safetynet.alerts.api.model.MedicalRecord;
import com.safetynet.alerts.api.model.dto.MedicalRecordDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class MedicalRecordDtoMapper implements IDtoMapper<MedicalRecord, MedicalRecordDto> {
    @Override
    public MedicalRecordDto mapToDto(MedicalRecord medicalRecord) {
        return new MedicalRecordDto(
                medicalRecord.getBirthdate(),
                medicalRecord.getMedications(),
                medicalRecord.getAllergies());
    }
}
