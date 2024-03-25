package edu.example.hw1_s2.repository;

import edu.example.hw1_s2.entity.OperationEntity;
import edu.example.hw1_s2.entity.OperationEntity.OperationType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OperationRepository extends MongoRepository<OperationEntity, String> {

    List<OperationEntity> getAllByType(OperationType operationType);

}
