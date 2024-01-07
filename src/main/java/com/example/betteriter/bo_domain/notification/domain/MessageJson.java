package com.example.betteriter.bo_domain.notification.domain;

import com.example.betteriter.global.common.converter.JsonConverter;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
public class MessageJson {
    private Long id;
    private String title;
    private String content;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageJson that = (MessageJson) o;
        return  Objects.equals(id, that.id) &&
                Objects.equals(title, that.title) &&
                Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content);
    }

    public static MessageJson of(Long id, String title, String content) {
        MessageJson messageJson = new MessageJson();
        messageJson.id = id;
        messageJson.title = title;
        messageJson.content = content;
        return messageJson;
    }

    public static class MessageJsonConverter extends JsonConverter<MessageJson> {}
}
