package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.QuestionnaireStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionnaireStatisticsRepository extends JpaRepository<QuestionnaireStatistics, Integer>,JpaSpecificationExecutor<QuestionnaireStatistics> {
    QuestionnaireStatistics findByTestExerciseTemplateIdAndClass1AndOptionOrder(@Param("testExerciseTemplateId") Integer testExerciseTemplateId, @Param("class1") String class1, @Param("optionOrder") String optionOrder);

    @Query("select sum(selectNumber) from QuestionnaireStatistics where testExerciseTemplateId = :testExerciseTemplateId and class1 = :class1 and optionOrder = :optionOrder")
    Integer findSelectNumberByTestExerciseTemplateIdAndClass1AndOptionOrder(@Param("testExerciseTemplateId") Integer testExerciseTemplateId, @Param("class1") String class1, @Param("optionOrder") String optionOrder);

    @Query("select sum(selectNumber) from QuestionnaireStatistics where testExerciseTemplateId = :testExerciseTemplateId and grade = :grade and optionOrder = :optionOrder")
    Integer findSelectNumberByTestExerciseTemplateIdAndGradeAndOptionOrder(@Param("testExerciseTemplateId") Integer testExerciseTemplateId, @Param("grade") String grade, @Param("optionOrder") String optionOrder);
}
