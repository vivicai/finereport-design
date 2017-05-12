package com.fr.design.mainframe.alphafine.searchManager;

import com.fr.design.mainframe.alphafine.AlphaFineHelper;
import com.fr.design.mainframe.alphafine.cell.cellModel.MoreModel;
import com.fr.design.mainframe.alphafine.model.SearchResult;
import com.fr.general.Inter;

import java.io.*;

/**
 * Created by XiaXiang on 2017/3/28.
 */
public class AlphaSearchManager implements AlphaFineSearchProcessor {
    private static AlphaSearchManager searchManager;
    private static PluginSearchManager pluginSearchManager;
    private static DocumentSearchManager documentSearchManager;
    private static FileSearchManager fileSearchManager;
    private static ActionSearchManager actionSearchManager;
    private static ConcludeSearchManager concludeSearchManager;
    private static LatestSearchManager latestSearchManager;

    public synchronized static AlphaSearchManager getSearchManager() {
        init();
        return searchManager;

    }

    private synchronized static void init() {
        if (searchManager == null) {
            searchManager = new AlphaSearchManager();
            pluginSearchManager = PluginSearchManager.getPluginSearchManager();
            documentSearchManager = DocumentSearchManager.getDocumentSearchManager();
            fileSearchManager = FileSearchManager.getFileSearchManager();
            actionSearchManager = ActionSearchManager.getActionSearchManager();
            concludeSearchManager = ConcludeSearchManager.getConcludeSearchManager();
            latestSearchManager = LatestSearchManager.getLatestSearchManager();
        }
    }

    @Override
    public synchronized SearchResult showLessSearchResult(String searchText) {
        SearchResult latestModelList = latestSearchManager.showLessSearchResult(searchText);
        SearchResult concludeModelList = concludeSearchManager.showLessSearchResult(searchText);
        SearchResult actionModelList = actionSearchManager.showLessSearchResult(searchText);
        SearchResult fileModelList = fileSearchManager.showLessSearchResult(searchText);
        SearchResult documentModelList = documentSearchManager.showLessSearchResult(searchText);
        SearchResult pluginModelList = pluginSearchManager.showLessSearchResult(searchText);
        latestModelList.addAll(concludeModelList);
        latestModelList.addAll(actionModelList);
        latestModelList.addAll(fileModelList);
        latestModelList.addAll(documentModelList);
        latestModelList.addAll(pluginModelList);
        return latestModelList;
    }

    public SearchResult showDefaultSearchResult() {
        SearchResult searchResult = new SearchResult();
        searchResult.add(new MoreModel(Inter.getLocText("FR-Designer_AlphaFine_Latest")));
        searchResult.add(new MoreModel(Inter.getLocText("FR-Designer_AlphaFine_Conclude")));
        searchResult.add(new MoreModel(Inter.getLocText("FR-Designer_Set")));
        searchResult.add(new MoreModel(Inter.getLocText("FR-Designer_Templates")));
        searchResult.add(new MoreModel(Inter.getLocText("FR-Designer_COMMUNITY_HELP")));
        searchResult.add(new MoreModel(Inter.getLocText("FR-Designer-Plugin_Addon")));
        return searchResult;
    }

    @Override
    public SearchResult showMoreSearchResult() {
        return null;
    }

    public SearchResult getLatestSearchResult() {
        ObjectInputStream is;
        SearchResult searchResult;
        try {
            is = new ObjectInputStream(new FileInputStream(AlphaFineHelper.getInfoFile()));
            searchResult = (SearchResult) is.readObject();

        } catch (IOException e) {
            searchResult = new SearchResult();
        } catch (ClassNotFoundException e) {
            searchResult = new SearchResult();

        }
        return searchResult;

    }

}