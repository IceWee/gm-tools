package bing.cqby.domain;

import javafx.scene.control.CheckBox;
import lombok.Data;

/**
 * 物品
 *
 * @author: bing
 * @date: 2019-01-01
 */
@Data
public class Item {

    private CheckBox check = new CheckBox();

    private Long entry;

    private String name1;

    private String description;

}
