package br.com.adagio.adagioagendadigital.ui.activities.main.fragments.tasks;

import android.util.Log;

import java.util.ArrayList;

public class TaskStaticValues {

    public static boolean CONTAINER_OPTIONS_IS_GONE = false;

    public static int LIMIT_LIST = 10;
    public static int OFFSET_LIST= 0;
    public static int NEXT_POSSIBLE_QUANTITY = LIMIT_LIST;
    public static int CURRENT_PAGE = 1;
    public static int AUX_OFFSET_OF_REST_AFTER_TODAY = OFFSET_LIST;
    public static ArrayList<Integer> AUX_OFFSET_OF_REST_AFTER_TODAY_MEMO = new ArrayList<>();

    public static void setContainerOptionsIsGone(boolean isGone){
        CONTAINER_OPTIONS_IS_GONE = isGone;
    }

    public static void setLimitList(int limit){
        LIMIT_LIST = limit;
        NEXT_POSSIBLE_QUANTITY = LIMIT_LIST + OFFSET_LIST;
    }

    public static void setOffsetList(int offset){
        if(offset < OFFSET_LIST){
            removeLastFromAuxOffsetOfRestAfterTodayMemo();
        }

        OFFSET_LIST = offset;
        NEXT_POSSIBLE_QUANTITY = OFFSET_LIST + LIMIT_LIST;
        CURRENT_PAGE = NEXT_POSSIBLE_QUANTITY / LIMIT_LIST;
    }

    public static void addToAuxOfRestAfterTodayMemo(int offset){
        int offsetToUpdate = offset;

        if(AUX_OFFSET_OF_REST_AFTER_TODAY_MEMO.size() == 0){

            if(!AUX_OFFSET_OF_REST_AFTER_TODAY_MEMO.contains(offset)){
                AUX_OFFSET_OF_REST_AFTER_TODAY_MEMO.add(offset);
            }

        } else {
            offsetToUpdate = AUX_OFFSET_OF_REST_AFTER_TODAY_MEMO.get(
                    AUX_OFFSET_OF_REST_AFTER_TODAY_MEMO.size()-1
            ) + offset;

            if(!AUX_OFFSET_OF_REST_AFTER_TODAY_MEMO.contains(offsetToUpdate)){
                AUX_OFFSET_OF_REST_AFTER_TODAY_MEMO.add(offsetToUpdate);
            }
        }

        AUX_OFFSET_OF_REST_AFTER_TODAY = offsetToUpdate;

    }

    public static void removeLastFromAuxOffsetOfRestAfterTodayMemo(){
        if(AUX_OFFSET_OF_REST_AFTER_TODAY_MEMO.size() > 0){

            AUX_OFFSET_OF_REST_AFTER_TODAY_MEMO.remove(
                    AUX_OFFSET_OF_REST_AFTER_TODAY_MEMO.get(AUX_OFFSET_OF_REST_AFTER_TODAY_MEMO.size() -1)
            );

            if(AUX_OFFSET_OF_REST_AFTER_TODAY_MEMO.size() >0){
                AUX_OFFSET_OF_REST_AFTER_TODAY=AUX_OFFSET_OF_REST_AFTER_TODAY_MEMO.get(
                        AUX_OFFSET_OF_REST_AFTER_TODAY_MEMO.size() -1
                );

            }

        }
    }

    public static int returnPreviousMemberOfLastFromAuxOffsetOfRestAfterTodayMemo(int member){
        return AUX_OFFSET_OF_REST_AFTER_TODAY_MEMO.get(
                AUX_OFFSET_OF_REST_AFTER_TODAY_MEMO.indexOf(
                        member
                ) - 1
        );
    }

    private static void printMemoAux(){
        for(Integer i : AUX_OFFSET_OF_REST_AFTER_TODAY_MEMO){
            Log.i("AUX VALUE", i+"");
        }
        Log.i("end", "--------------------------------------------");
    }

    public static void goBackToDefaultValue(){
        LIMIT_LIST = 10;
        OFFSET_LIST = 0;
        NEXT_POSSIBLE_QUANTITY = LIMIT_LIST;
        CURRENT_PAGE = 1;
        AUX_OFFSET_OF_REST_AFTER_TODAY = OFFSET_LIST;
        AUX_OFFSET_OF_REST_AFTER_TODAY_MEMO = new ArrayList<>();
    }
}
