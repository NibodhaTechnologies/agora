package com.nibodha.agora.services.objectmapper.transformer;

import com.nibodha.agora.services.objectmapper.config.Mapping;

public interface Transformer<S, T> {

    T transform(S source, Mapping mappingConfig) throws Exception;

}
