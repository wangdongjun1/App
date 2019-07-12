package com.jarisoft.util;

import android.text.method.ReplacementTransformationMethod;

/**
 * Created by robot on 2019/6/27.
 */

public class AllCapTransformation extends ReplacementTransformationMethod {
    @Override
    protected char[] getOriginal() {
        char[] aa= { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z' };
        return aa;
    }

    @Override
    protected char[] getReplacement() {
        char[] AA= { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z' };
        return AA;
    }
}
