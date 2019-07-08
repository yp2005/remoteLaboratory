package com.remoteLaboratory.repositories;


import com.remoteLaboratory.entities.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Integer>,JpaSpecificationExecutor<Reply> {
    void deleteBySubjectId(@Param("subjectId") Integer subjectId);

    void deleteByReplayId(@Param("replayId") Integer replayId);
}
