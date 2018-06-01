package com.codingame.view.tooltip;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.codingame.gameengine.core.AbstractPlayer;
import com.codingame.gameengine.core.GameManager;
import com.codingame.gameengine.core.Module;
import com.codingame.gameengine.module.entities.Entity;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.google.inject.Inject;

public class TooltipModule implements Module {

  GameManager<AbstractPlayer> gameManager;
  @Inject
  GraphicEntityModule entityModule;
  Map<Integer, Map<String, Object>> registrations;
  Map<Integer, Map<String, Object>> newRegistrations;
  Map<Integer, String[]> extra, newExtra;

  @Inject
  TooltipModule(GameManager<AbstractPlayer> gameManager) {
    this.gameManager = gameManager;
    gameManager.registerModule(this);
    registrations = new HashMap<>();
    newRegistrations = new HashMap<>();
    extra = new HashMap<>();
    newExtra = new HashMap<>();
  }

  @Override
  public void onGameInit() {
    sendFrameData();
  }

  @Override
  public void onAfterGameTurn() {
    sendFrameData();
  }

  @Override
  public void onAfterOnEnd() {
  }

  private void sendFrameData() {
    Object[] data = { registrations, newExtra };
    gameManager.setViewData("tooltips", data);

    newRegistrations.clear();
    newExtra.clear();
  }

  public void registerEntity(int id, Map<String, Object> params) {
    if (!params.equals(registrations.get(id))) {
      newRegistrations.put(id, params);
      registrations.put(id, params);
    }
  }

  public Map<String, Object> getParams(int id) {
    Map<String, Object> params = registrations.get(id);
    if (params == null) {
      params = new HashMap<>();
      registrations.put(id, params);
    }
    return params;
  }

  public void updateExtraTooltipText(Entity<?> entity, String... lines) {
    int id = entity.getId();
    if (!Arrays.equals(lines, extra.get(id))) {
      newExtra.put(id, lines);
      extra.put(id, lines);
    }
  }
}
