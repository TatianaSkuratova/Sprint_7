package models;

import lombok.Getter;
import lombok.Setter;
import models.AvailableStation;
import models.Order;
import models.PageInfo;

@Getter
@Setter
public class Orders {

 private Order[] orders;
 private PageInfo pageInfo;
 private AvailableStation[] availableStations;

}
