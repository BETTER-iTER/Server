package com.example.betteriter.global.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Job {
    SW_DEVELOPER("SW 개발자"),
    GAME_DEVELOPER("게임 개발자"),
    STUDENT("학생"),
    TEACHER("선생님"),
    VIDEO_DESIGNER("영상 디자이너"),
    VISUAL_DESIGNER("시각 디자이너"),
    DATA_ANALYST("데이터 분석가"),
    PLANNER("기획자"),
    EDITOR("에디터"),
    CEO("CEO");
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
