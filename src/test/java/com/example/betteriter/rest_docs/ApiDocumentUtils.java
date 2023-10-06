package com.example.betteriter.rest_docs;

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

public interface ApiDocumentUtils {

    static OperationRequestPreprocessor getDocumentRequest() {
        return preprocessRequest(
                modifyUris() // 문서상 uri를 변경하는 메소드
                        .scheme("https")
                        .host("betteritem.store")
                        .removePort(),
                prettyPrint()); // 문서의 request를 예쁘게 출력해주는 메소드
    }

    static OperationResponsePreprocessor getDocumentResponse() {
        return preprocessResponse(prettyPrint());
    }
}