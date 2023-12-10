package com.example.betteriter.global.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Job {
    DEVELOPER("개발자"),
    STUDENT("학생"),
    TEACHER("선생님"),
    DESIGNER("디자이너");
    private final String jobName;

    @JsonCreator
    public static Job from(String name) {
        for (Job job : Job.values()) {
            if (job.getJobName().equals(name)) {
                return job;
            }
        }
        throw new IllegalArgumentException("일치하는 Job 정보가 존재하지 않습니다.");
    }

    @JsonValue
    public String getJobName() {
        return jobName;
    }
}
