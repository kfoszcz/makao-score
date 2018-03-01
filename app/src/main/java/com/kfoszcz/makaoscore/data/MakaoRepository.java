package com.kfoszcz.makaoscore.data;

import java.util.List;

/**
 * Created by Krzysztof on 2018-03-01.
 */

public class MakaoRepository {

    private final MakaoDao makaoDao;

    public MakaoRepository(MakaoDao makaoDao) {
        this.makaoDao = makaoDao;
    }

    public List<Player> getAllPlayers() {
        return makaoDao.getAllPlayers();
    }

}
