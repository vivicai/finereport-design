package com.fr.design.mainframe.alphafine.search.manager;

import com.fr.design.mainframe.alphafine.model.SearchResult;

/**
 * Created by XiaXiang on 2017/3/27.
 */
public interface AlphaFineSearchProcessor {
    /**
     * 获取默认显示条数
     *
     * @param searchText
     * @return
     */
    SearchResult getLessSearchResult(String searchText);

    /**
     * 获取剩余条数
     *
     * @return
     */
    SearchResult getMoreSearchResult();
}
