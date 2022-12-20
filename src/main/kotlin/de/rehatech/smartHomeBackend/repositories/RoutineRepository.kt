package de.rehatech.smartHomeBackend.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface RoutineRepository : CrudRepository<String, String> {

}