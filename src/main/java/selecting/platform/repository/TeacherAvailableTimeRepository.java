package selecting.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import selecting.platform.model.TeacherAvailableTime;
import selecting.platform.model.User;

import java.util.List;

@Repository
public interface TeacherAvailableTimeRepository extends JpaRepository<TeacherAvailableTime, Integer> {

    List<TeacherAvailableTime> findByTeacher(User teacher);
}
