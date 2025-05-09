package selecting.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import selecting.platform.model.TeacherAvailableTime;
import selecting.platform.model.User;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface TeacherAvailableTimeRepository extends JpaRepository<TeacherAvailableTime, Integer> {

    List<TeacherAvailableTime> findByTeacher(User teacher);

    boolean existsByTeacherAndDayOfWeekAndStartTimeLessThanEqualAndEndTimeGreaterThan( User teacher,
                                                                                       DayOfWeek dayOfWeek,
                                                                                       LocalTime startTime,
                                                                                       LocalTime endTime);

    List<TeacherAvailableTime> findByTeacherAndDayOfWeek(User teacher, DayOfWeek dayOfWeek);
}
