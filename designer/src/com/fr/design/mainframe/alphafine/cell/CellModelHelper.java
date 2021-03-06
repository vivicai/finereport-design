package com.fr.design.mainframe.alphafine.cell;

import com.fr.design.mainframe.alphafine.CellType;
import com.fr.design.mainframe.alphafine.cell.model.AlphaCellModel;
import com.fr.design.mainframe.alphafine.search.manager.ActionSearchManager;
import com.fr.design.mainframe.alphafine.search.manager.DocumentSearchManager;
import com.fr.design.mainframe.alphafine.search.manager.FileSearchManager;
import com.fr.design.mainframe.alphafine.search.manager.PluginSearchManager;
import com.fr.json.JSONObject;

/**
 * Created by XiaXiang on 2017/5/17.
 */
public class CellModelHelper {
    private static final String RESULT = "result";

    public static AlphaCellModel getModelFromJson(JSONObject object) {
        int typeValue = object.optInt("cellType");
        AlphaCellModel cellModel = null;
        switch (CellType.parse(typeValue)) {
            case ACTION:
                cellModel = ActionSearchManager.getModelFromCloud(object.optJSONObject(RESULT));
                break;
            case DOCUMENT:
                cellModel = DocumentSearchManager.getModelFromCloud(object.optJSONObject(RESULT));
                break;
            case FILE:
                cellModel = FileSearchManager.getModelFromCloud(object.optJSONObject(RESULT));
                break;
            case PLUGIN:
            case REUSE:
                cellModel = PluginSearchManager.getModelFromCloud(object.optJSONObject(RESULT));
                break;

        }
        return cellModel;
    }

    public static String getResultValueFromModel(AlphaCellModel cellModel) {
        return cellModel.getStoreInformation();
    }
}
