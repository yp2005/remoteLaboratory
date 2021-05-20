package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.QuestionnaireStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionnaireStatisticsRepository extends JpaRepository<QuestionnaireStatistics, Integer>,JpaSpecificationExecutor<QuestionnaireStatistics> {
    QuestionnaireStatistics findByTestExerciseTemplateIdAndClass1AndOption(@Param("testExerciseTemplateId") Integer testExerciseTemplateId, @Param("class1") String class1, @Param("option") String option);
}
