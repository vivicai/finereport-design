/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.menu;

import com.fr.general.ComparatorUtils;
import com.fr.general.GeneralContext;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.Locale;

import static com.fr.design.gui.syntax.ui.rtextarea.RTADefaultInputMap.DEFAULT_MODIFIER;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-3-13
 * Time: 下午4:11
 */
public class KeySetUtils {



    public static final MenuKeySet OPEN_TEMPLATE = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'O';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("M-Open_Report");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return KeyStroke.getKeyStroke(KeyEvent.VK_O, DEFAULT_MODIFIER);
        }
    };


    public static final MenuKeySet RECENT_OPEN = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'E';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("M-Open_Recent");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    public static final MenuKeySet CLOSE_CURRENT_TEMPLATE = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'W';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("FR-Designer_Close");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return KeyStroke.getKeyStroke(KeyEvent.VK_W, DEFAULT_MODIFIER);
        }
    };

    public static final MenuKeySet SAVE_TEMPLATE = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'S';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("FR-Designer_Save");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return KeyStroke.getKeyStroke(KeyEvent.VK_S, DEFAULT_MODIFIER);
        }
    };

    public static final MenuKeySet SAVE_AS_TEMPLATE = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'A';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("M-Save_As");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    public static final MenuKeySet UNDO = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'U';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("M_Edit-Undo");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return KeyStroke.getKeyStroke(KeyEvent.VK_Z, DEFAULT_MODIFIER);
        }
    };

    public static final MenuKeySet REDO = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'R';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("M_Edit-Redo");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return KeyStroke.getKeyStroke(KeyEvent.VK_Y, DEFAULT_MODIFIER);
        }
    };

    public static final MenuKeySet EXPORT = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'E';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("FR-Designer_Export");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return KeyStroke.getKeyStroke(KeyEvent.VK_E, DEFAULT_MODIFIER);
        }
    };

    public static final MenuKeySet EXCEL_EXPORT = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'E';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("M_File-Export-Excel");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };


    public static final MenuKeySet PAGE_EXCEL_EXPORT = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'P';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("FR-Engine_Export-Excel-Page");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };


    public static final MenuKeySet SIMPLE_EXCEL_EXPORT = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'O';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("FR-Engine_Export-Excel-Simple");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };


    public static final MenuKeySet PAGETOSHEET_EXCEL_EXPORT = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'S';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("FR-Engine_Export-Excel-PageToSheet");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    public static final MenuKeySet PDF_EXPORT = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'P';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("M_File-Export-PDF");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };


    public static final MenuKeySet WORD_EXPORT = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'W';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("M_File-Export-Word");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };


    public static final MenuKeySet SVG_EXPORT = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'S';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("M_File-Export-SVG");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };


    public static final MenuKeySet CSV_EXPORT = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'C';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("M_File-Export-CSV");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };


    public static final MenuKeySet TEXT_EXPORT = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'T';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("M_File-Export-Text");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };


    public static final MenuKeySet EMBEDDED_EXPORT = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'T';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("M_File-ExportT-Template(embedded_data)");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };



    public static final MenuKeySet IMAGE_EXPORT = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'M';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText(new String[]{"Image","Format"});
        }

        @Override
        public KeyStroke getKeyStroke() {
            return KeyStroke.getKeyStroke(KeyEvent.VK_M, DEFAULT_MODIFIER);
        }
    };

    public static final MenuKeySet EXPORT_CHART = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'E';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("FR-Designer_ExportAndOutput");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return KeyStroke.getKeyStroke(KeyEvent.VK_E, DEFAULT_MODIFIER);
        }
    };

    public static final MenuKeySet EXCEL_EXPORT_CHART = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'E';
        }

        @Override
        public String getMenuName() {
            return "EXCEL" + Inter.getLocText("FR-Designer_ExportAndOutput");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return KeyStroke.getKeyStroke(KeyEvent.VK_E, DEFAULT_MODIFIER);
        }
    };

    public static final MenuKeySet PREFERENCE = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'P';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("M_Window-Preference");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    public static final MenuKeySet SWITCH_ENV = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 0;
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("FR-Designer_M-SwitchWorkspace");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    public static final MenuKeySet EXIT_DESIGNER = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'X';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("FR-Designer_M-Exit");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };


    public static final MenuKeySet TEMPLATE = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'T';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("FR-Designer_M-Template");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };


    public static final MenuKeySet TEMPLATE_TABLE_DATA_SOURCE = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'D';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("DS-Report_TableData");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    public static final MenuKeySet REPORT_WEB_ATTR = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'W';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("Report-Web_Attributes");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };


    public static final MenuKeySet REPORT_EXPORT_ATTR = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'E';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("ReportD-Excel_Export");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    public static final MenuKeySet REPORT_PARAMETER_ATTR = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'A';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("M_Report-Report_Parameter");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };


    public static final MenuKeySet REPORT_PAGE_SETUP = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'U';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("M-Page_Setup");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    public static final MenuKeySet REPORT_HEADER = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'H';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("M_Report-Report_Header");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    public static final MenuKeySet REPORT_FOOTER = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'F';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("M_Report-Report_Footer");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    public static final MenuKeySet REPORT_BACKGROUND = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'B';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("M_Report-Report_Background");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };


    public static final MenuKeySet REPORT_WRITE = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'R';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("Report-Write_Attributes");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    public static final MenuKeySet REPORT_COLUMN = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'C';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("M_Report-Report_Columns");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    public static final MenuKeySet EC_COLUMNS = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'C';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("FR-Designer_ReportColumns-Columns");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };


    public static final MenuKeySet REPORT_PAGE = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'G';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("FR-Designer_M-Repeat-Freeze");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };


    public static final MenuKeySet EC_FROZEN = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'G';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("FR-Engine_Frozen");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };


    public static final MenuKeySet REPORT_ENGINE = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'E';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText(new String[]{"Report_Engine", "Attribute"});
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    public static final MenuKeySet ALLOW_AUTHORITY_EDIT = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 0;
        }

        @Override
        public String getMenuName() {

            // mod by anchore 16/11/16
            if (ComparatorUtils.equals(Locale.JAPAN, GeneralContext.getLocale())) {
                return Inter.getLocText(new String[]{"FR-Engine_RP_Authority_Edit", "Allow"});
            } else {
                return Inter.getLocText(new String[]{"Allow", "FR-Engine_RP_Authority_Edit"});
            }
            //return Inter.getLocText(new String[]{"Allow", "DashBoard-Potence", "Edit"});
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    public static final MenuKeySet CELL = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'C';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("FR-Designer_Cell");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    public static final MenuKeySet CELL_EXPAND_ATTR = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'E';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("FR-Designer_Expand");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    public static final MenuKeySet CELL_WIDGET_ATTR = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'W';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("FR-Designer_Widget-Settings");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    public static final MenuKeySet GLOBAL_STYLE = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'S';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("M_Format-Style");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    public static final MenuKeySet CONDITION_ATTR = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'C';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("FR-Designer_Condition_Attributes");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    public static final MenuKeySet PRESENT = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'P';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("FR-Chart-Style_Present");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    public static final MenuKeySet DATA_DICT = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'D';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("M_Format-Data_Map");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    public static final MenuKeySet FORMULA_PRESENT = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'F';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("Present-Formula_Present");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    public static final MenuKeySet BARCODE = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'B';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("M_Insert-Barcode");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    public static final MenuKeySet CURRENCY_LINE = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'L';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("FR-Designer_Currency_Line");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };


    public static final MenuKeySet NO_PRESENT = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'N';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("Present-No_Present");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    public static final MenuKeySet HYPER_LINK = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'K';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("M_Insert-Hyperlink");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    public static final MenuKeySet MERGE_CELL = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'M';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("M_Edit-Merge_Cell");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    public static final MenuKeySet UNMERGE_CELL = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'G';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("M_Edit-Unmerge_Cell");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    public static final MenuKeySet CELL_OTHER_ATTR = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'A';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("FR-Designer_Other");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    public static final MenuKeySet CELL_ELEMENT = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 0;
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("M_Insert-Cell");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    public static final MenuKeySet INSERT_DATA_COLUMN = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'D';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("M_Insert-Data_Column");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };










    public static final MenuKeySet INSERT_FLOAT = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 0;
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("M_Insert-Float");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };







































}