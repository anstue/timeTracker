package com.ti_zero.com.apptime.ui.builder;

import com.ti_zero.com.apptime.data.objects.AbstractItem;
import com.ti_zero.com.apptime.data.objects.AccountItem;
import com.ti_zero.com.apptime.data.objects.GroupItem;
import com.ti_zero.com.apptime.data.objects.ObservableWithUUID;
import com.ti_zero.com.apptime.data.objects.TimeEntry;
import com.ti_zero.com.apptime.helper.LogTag;
import com.ti_zero.com.apptime.helper.Logging;
import com.ti_zero.com.apptime.ui.objects.AgendaItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by anstue on 1/24/18.
 */

public class AgendaItemBuilder {

    private GroupItem groupItem;

    public AgendaItemBuilder(GroupItem groupItem) {
        this.groupItem = groupItem;
    }

    /**
     * Builds a list of agenda items from the given groupItem
     */
    public List<AgendaItem> build() {
        List<AgendaItem> agendaItems = new ArrayList<>();
        agendaItems = createAgendaItems(agendaItems, groupItem, "");
        Collections.sort(agendaItems);
        return agendaItems;
    }

    private List<AgendaItem> createAgendaItems(List<AgendaItem> agendaItems, AbstractItem mainItem, String groupName) {
        if (mainItem.getChildren() != null) {
            for (AbstractItem item : mainItem.getChildren()) {
                createAgendaItems(agendaItems, item, groupName + ((!"".equals(groupName)) ? " - " : "") + getNameButNotRoot(mainItem));
            }
        } else if (mainItem instanceof AccountItem) {
            for (TimeEntry timeEntry : ((AccountItem) mainItem).getTimeEntries()) {
                agendaItems.add(new AgendaItem(mainItem.getUniqueID(), groupName, mainItem.getName(), timeEntry.getStart(), timeEntry.getEnd()));
            }
        } else {
            Logging.logError(LogTag.UI, "Unrecognized Item Type found in createAgendaItems");
        }
        return agendaItems;
    }

    private String getNameButNotRoot(AbstractItem mainItem) {
        return (mainItem.getUniqueID() == ObservableWithUUID.ROOT_UUID) ? "" : mainItem.getName();
    }
}
