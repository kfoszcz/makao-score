package com.kfoszcz.makaoscore.view;

import com.kfoszcz.makaoscore.data.Player;

/**
 * Created by Krzysztof on 2018-03-02.
 */

public interface AddPlayerInterface {

    void setFormFields(Player player);

    void finishActivity();

    void showErrorToast(String message);
}
