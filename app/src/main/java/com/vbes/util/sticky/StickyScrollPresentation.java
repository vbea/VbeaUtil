package com.vbes.util.sticky;

/**
 * Created by Vbe on 2020/12/17.
 */
public interface StickyScrollPresentation {

    void freeHeader();
    void freeFooter();
    void stickHeader(int translationY);
    void stickFooter(int translationY);

    void initHeaderView(int id);
    void initFooterView(int id);
}
