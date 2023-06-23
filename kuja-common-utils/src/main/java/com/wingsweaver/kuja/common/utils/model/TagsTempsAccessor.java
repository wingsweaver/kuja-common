package com.wingsweaver.kuja.common.utils.model;

import lombok.Getter;

/**
 * {@link AbstractTagsTemps} 的访问器。
 *
 * @author wingsweaver
 */
@Getter
public class TagsTempsAccessor {
    private final AbstractTagsTemps tagsTemps;

    public TagsTempsAccessor(AbstractTagsTemps tagsTemps) {
        this.tagsTemps = tagsTemps;
    }
}
