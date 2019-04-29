package com.sevenstringedzithers.sitong.utils;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.sevenstringedzithers.sitong.R;
import com.sevenstringedzithers.sitong.view.expandable.model.MyChild;
import com.sevenstringedzithers.sitong.view.expandable.model.MyParent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by jhj_Plus on 2016/9/2.
 */
public class AppUtil {
    private static final String TAG = "AppUtil";

    private static Random sRandom = new Random();

    private static List<int[]> parentListRight = new ArrayList<>();
    private static List<String> parentListRightDescribe = new ArrayList<>();
    private static List<Integer> parentListRightAll = new ArrayList<>();
    private static List<String> childListRightDescribe = new ArrayList<>();
    private static List<Integer> childListRightAllImage = new ArrayList<>();
    private static String nextLine="\n";
    private static String start="︻释义︼"+nextLine;


    private static List<int[]> parentListLeft = new ArrayList<>();
    private static List<String> parentListLeftDescribe = new ArrayList<>();
    private static List<Integer> parentListLeftAll = new ArrayList<>();
    private static List<String> childListLeftDescribe = new ArrayList<>();
    private static List<Integer> childListLeftAllImage = new ArrayList<>();

    public static final int[] TYPE_PARENT = {R.layout.item_parent_1, R.layout.item_parent_2};
    public static final int[] TYPE_CHILD = {R.layout.item_child_1, R.layout.item_child_2};

    public static boolean checkLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    // sRandom.nextInt(16) + 5
    public static ArrayList<MyParent> getListData(int type) {
        if (type == 0) {
//            右手势

            if (parentListRightAll.size() <= 0) {
                parentListRightAll.add(R.mipmap.ic_right_tuo_normal);
                parentListRightAll.add(R.mipmap.ic_right_tuo_pressed);

                parentListRightAll.add(R.mipmap.ic_right_pi_normal);
                parentListRightAll.add(R.mipmap.ic_right_pi_pressed);

                parentListRightAll.add(R.mipmap.ic_right_tiao2_normal);
                parentListRightAll.add(R.mipmap.ic_right_tiao2_pressed);

                parentListRightAll.add(R.mipmap.ic_right_mo_normal);
                parentListRightAll.add(R.mipmap.ic_right_mo_pressed);

                parentListRightAll.add(R.mipmap.ic_right_ti_normal);
                parentListRightAll.add(R.mipmap.ic_right_ti_pressed);

                parentListRightAll.add(R.mipmap.ic_right_gou_normal);
                parentListRightAll.add(R.mipmap.ic_right_gou_pressed);

                parentListRightAll.add(R.mipmap.ic_right_zhai_normal);
                parentListRightAll.add(R.mipmap.ic_right_zhai_pressed);

                parentListRightAll.add(R.mipmap.ic_right_big_zuo_normal);
                parentListRightAll.add(R.mipmap.ic_right_big_zuo_pressed);

                parentListRightAll.add(R.mipmap.ic_right_fan_zuo_normal);
                parentListRightAll.add(R.mipmap.ic_right_fan_zuo_pressed);

                parentListRightAll.add(R.mipmap.ic_right_small_zuo_normal);
                parentListRightAll.add(R.mipmap.ic_right_small_zuo_pressed);

                parentListRightAll.add(R.mipmap.ic_right_lunzhi_normal);
                parentListRightAll.add(R.mipmap.ic_right_lunzhi_pressed);
            }

            if (parentListRight.size() > 0) {
                parentListRight.clear();
            }
            for (int i = 0; i < parentListRightAll.size(); i += 2) {
                int[] image = {parentListRightAll.get(i), parentListRightAll.get(i + 1)};
                parentListRight.add(image);
            }
            childListRightAllImage.add(R.mipmap.ic_right_child_tuo);
            childListRightAllImage.add(R.mipmap.ic_right_child_pi);
            childListRightAllImage.add(R.mipmap.ic_right_child_tiao);
            childListRightAllImage.add(R.mipmap.ic_right_child_mo);
            childListRightAllImage.add(R.mipmap.ic_right_child_ti);
            childListRightAllImage.add(R.mipmap.ic_right_child_gou);
            childListRightAllImage.add(R.mipmap.ic_right_child_zhai);
            childListRightAllImage.add(R.mipmap.ic_right_child_da);
            childListRightAllImage.add(R.mipmap.ic_right_child_dazuo_xiaozuo);
            childListRightAllImage.add(R.mipmap.ic_right_child_fanzuo);
            childListRightAllImage.add(R.mipmap.ic_right_child_dazuo_xiaozuo);
            childListRightAllImage.add(R.mipmap.ic_right_child_lunzhi);


            parentListRightDescribe.add("托");
            parentListRightDescribe.add("劈");
            parentListRightDescribe.add("挑");
            parentListRightDescribe.add("抹");
            parentListRightDescribe.add("剔");
            parentListRightDescribe.add("勾");
            parentListRightDescribe.add("摘");
            parentListRightDescribe.add("打");
            parentListRightDescribe.add("大撮");
            parentListRightDescribe.add("反撮");
            parentListRightDescribe.add("小撮");
            parentListRightDescribe.add("轮指");


            childListRightDescribe.add(start+"将大指倒竖，虎口全开，大指指节伸直，指须靠絃，使絃从指面经甲尖向岳而出。其运动在大指末节并要用腕力配合。");
            childListRightDescribe.add(start+"将大指倒竖，微屈末节，大指倒竖之时，肘亦微微上抬。其运动在大指末节，宜微用腕力，甲尖着弦而入。");
            childListRightDescribe.add(start+"食指屈曲根、中两节，用大指甲尖轻抵食指箕斗中，使两指成一圈形，挑时大指伸直中末两节，并微运腕力将食指向前推送，以助食指自伸之力。以伸缩灵活为妙。其运动在大指中末二节和食指中节的伸屈，切不可将两指揑紧使其抵送不灵。");
            childListRightDescribe.add(start+"食指屈曲根节，伸直中、末二节，指头肉面箕斗中着絃，入弦稍稍深下。先肉后甲，由指面经甲尖中锋正直弹入，不可斜扫。其运动在臂、腕暗助食指中、末二节之力。");
            childListRightDescribe.add(start+"中指微曲中、末二节，甲背着弦，下指不可太深，太深就会滞碍。须当空落指，其运动在中、末二节伸直坚劲之力。方能得灵动之機；又须正锋弹出，得声才清和浑厚。");
            childListRightDescribe.add(start+"中指屈曲根、中二节，竖直末节，用指头抵絃，半肉半甲勾入。宜重下指，轻出弦，要纯用正锋。勾时须用腕、肘之力引之，勾后手指落在次弦之上，不必离开。");
            childListRightDescribe.add(start+"名指屈曲中节，竖直末节，用甲背向外出弦，运动也在根节，需要伶俐。其运动在臂、腕助末节劲挺之力。");
            childListRightDescribe.add(start+"名指微屈根节，竖直中、末二节，指头着弦，由根节运动与“勾”微有不同，多用于一、二弦。");
            childListRightDescribe.add(start+"运用指法“勾”和“托”同时弹奏得音。撮的指法需要稍稍将手臂和手肘关节抬起，以便于大指“托”的发力。");
            childListRightDescribe.add(start+"运用指法“剔”和“擘”同时弹奏两弦得音。撮的指法需要稍稍将手臂和手肘关节抬起，以便于大指“擘”的发力。");
            childListRightDescribe.add(start+"运用指法“勾”和“挑”同时弹奏得音。小撮中的“挑”和常规弹奏的“挑”略有不同，弹奏小撮时，需要提前将食指放在弦上，而且食指触弦部分是在指端，大指的圆（龙眼）依旧保持。");
            childListRightDescribe.add(start+"先将名中食三指半曲、跺齐、并紧，食指侧面中段紧靠于大指的前半段指腹，然后将手指停在所弹之弦的后面一根弦上（靠近身体），指尖落弦，然后名、中、食三指依次击弹前一根弦。击弦时，方向是前方稍偏右，三指应尽量击在弦上的同一个点，手腕应配合稍作转动。轮指七弦时悬空操作。");


            ArrayList<MyParent> myParents = new ArrayList<>();
            for (int i = 0; i < parentListRight.size(); i++) {
                MyParent myParent = getParent(type, i);
                myParent.setImage(parentListRight.get(i));
                myParent.setTitle(parentListRightDescribe.get(i));
                myParents.add(myParent);
            }
            return myParents;
        } else {
//            左手勢
            if (parentListLeftAll.size() <= 0) {
                parentListLeftAll.add(R.mipmap.ic_left_big_zhi_normal);
                parentListLeftAll.add(R.mipmap.ic_left_big_zhi_pressed);

                parentListLeftAll.add(R.mipmap.ic_left_shi_zhi_normal);
                parentListLeftAll.add(R.mipmap.ic_left_shi_zhi_pressed);

                parentListLeftAll.add(R.mipmap.ic_left_middle_normal);
                parentListLeftAll.add(R.mipmap.ic_left_middle_pressed);

                parentListLeftAll.add(R.mipmap.ic_left_wuming_zhi_normal);
                parentListLeftAll.add(R.mipmap.ic_left_wuming_zhi_pressed);

                parentListLeftAll.add(R.mipmap.ic_left_fan_zhi_normal);
                parentListLeftAll.add(R.mipmap.ic_left_fan_zhi_pressed);

                parentListLeftAll.add(R.mipmap.ic_left_gui_zhi_normal);
                parentListLeftAll.add(R.mipmap.ic_left_gui_zhi_pressed);


                parentListLeftAll.add(R.mipmap.ic_left_qia_qi_normal);
                parentListLeftAll.add(R.mipmap.ic_left_qia_qi_pressed);

                parentListLeftAll.add(R.mipmap.ic_left_dai_qi_normal);
                parentListLeftAll.add(R.mipmap.ic_left_dai_qi_pressed);
            }

            if (parentListLeft.size() > 0) {
                parentListLeft.clear();
            }

            for (int i = 0; i < parentListLeftAll.size(); i += 2) {
                int[] image = {parentListLeftAll.get(i), parentListLeftAll.get(i + 1)};
                parentListLeft.add(image);
            }
            childListLeftAllImage.add(R.mipmap.ic_left_child_dazhi);
            childListLeftAllImage.add(R.mipmap.ic_left_child_shizhi);
            childListLeftAllImage.add(R.mipmap.ic_left_child_zhongzhi);
            childListLeftAllImage.add(R.mipmap.ic_left_child_wumingzhi);
            childListLeftAllImage.add(R.mipmap.ic_left_child_fanzhi);
            childListLeftAllImage.add(R.mipmap.ic_left_child_guizhi);
            childListLeftAllImage.add(R.mipmap.ic_left_child_qiaqi);
            childListLeftAllImage.add(R.mipmap.ic_left_child_daiqi);


            parentListLeftDescribe.add("大指按弦");
            parentListLeftDescribe.add("食指");
            parentListLeftDescribe.add("中指");
            parentListLeftDescribe.add("无名指");
            parentListLeftDescribe.add("泛指");
            parentListLeftDescribe.add("跪指");
            parentListLeftDescribe.add("掐起");
            parentListLeftDescribe.add("罨");


            childListLeftDescribe.add(start+"大指稍屈中、末二节，虎口半开，以右侧近甲根半甲半肉处按弦。食指提起中、末二节微弯，中指直俯于食指下，名指也要伸直略高于中指，要使三指靠拢指缝，禁指（小指）打直翘起来。 ");
            childListLeftDescribe.add(start+"食指稍微弯屈它根节，中节、末节伸出。用指头肉面箕斗（箕斗：末节螺纹处）中平正按弦。所转动运用的力量主要是中、末二节发出的。大指稍微弯曲，侧伏在旁边，虎口稍微张开。中、名二指伸直，中指稍微高于食指、名指又稍微高于中指，指尖都微带仰（意会一下），禁指（小指）打直翘起来。");
            childListLeftDescribe.add(start+"按两弦，则稍偏向左目的是兼用手臂、手腕的力运转中、末两节灵。所转动运用的力量主要是中、末二节发出的。食、名二指平直并伸出，使名指高于中指，食指又稍微高于名指。大指靠向食指旁，指尖昂起，虎口前开后合。禁指（小指）打直翘起来。");
            childListLeftDescribe.add(start+"名指弯屈中节，紧靠中指，凸出其末节，用指头箕斗（箕斗：末节螺纹处）略偏左处按弦，假如同时按两三弦，那么就伸直中、末二节，同时也要稍偏于左来按弦。所转动运用的力量主要是名指末节所使出来的力量，大指稍屈中、末二节，靠向食指旁。食、中二指靠拢，中指稍弯曲，食指稍微提起，指尖要有落差层次的感觉。");
            childListLeftDescribe.add(start+"左手平伸、舒展，食、中、名三指用指肚触弦，大指用右侧面触弦，名指在九徽以外时可用左侧面触弦。配合右手拨弦时触弦即抬，如蜻蜓点水般。");
            childListLeftDescribe.add(start+"名指屈其中节，跪其末节，或用指背或用骨节处跪按弦上，用左侧按弦。手心保持近似握小球一样的饱满状态。");
            childListLeftDescribe.add(start+"大指与名指呈大“C”型，名指提前按实在相对高的徽外上，用左侧按弦，压低手腕；大指按在相对低的徽位上，然后用大指第一关节的指腹勾起琴弦，向上顺势带起发声。");
            childListLeftDescribe.add(start+"大指对相对低的徽位磕下，击弦有声，一般有名指先按于相对高的徽位。将指力集中凝于一点，只落于弦上，勿击响琴面，落后指即着实按紧，按紧初音俞清，如先无按弹，大指直接罨的为虚罨。");


            ArrayList<MyParent> myParents = new ArrayList<>();
            for (int i = 0; i < parentListLeft.size(); i++) {
                MyParent myParent = getParent(type, i);
                myParent.setImage(parentListLeft.get(i));
                myParent.setTitle(parentListLeftDescribe.get(i));
                myParents.add(myParent);
            }
            return myParents;
        }


    }

    @NonNull
    public static MyParent getParent(int type, int position) {
        MyParent myParent = new MyParent();
        myParent.setType(TYPE_PARENT[0]);
        if (type == 0) {
            ArrayList<MyChild> myChildren = new ArrayList<>();
            MyChild myChild = new MyChild();
            myChild.setType(TYPE_CHILD[0]);
            myChild.setImage(childListRightAllImage.get(position));
            myChild.setTitle(childListRightDescribe.get(position));
            myChildren.add(myChild);
            myParent.setMyChildren(myChildren);

        } else {
            ArrayList<MyChild> myChildren = new ArrayList<>();
            MyChild myChild = new MyChild();
            myChild.setType(TYPE_CHILD[0]);
            myChild.setImage(childListLeftAllImage.get(position));
            myChild.setTitle(childListLeftDescribe.get(position));
            myChildren.add(myChild);
            myParent.setMyChildren(myChildren);
        }
        return myParent;
    }

    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

}
