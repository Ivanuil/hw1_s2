package edu.example.hw1_s2.service;

import edu.example.hw1_s2.dto.OperationDto;
import edu.example.hw1_s2.entity.OperationEntity;
import edu.example.hw1_s2.repository.OperationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OperationService {

    private final OperationRepository operationRepository;

    public void logOperation(OperationDto operationDto) {
        operationRepository.save(new OperationEntity(null,
                operationDto.getContent(),
                operationDto.getTime(),
                operationDto.getType()));
    }

}
