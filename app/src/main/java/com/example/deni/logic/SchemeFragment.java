package com.example.deni.logic;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.deni.connect.Connect;
import com.example.deni.globalUtility.Constants;
import com.example.deni.logicComponentModel.single.BasicComponentDefault;
import com.example.deni.logicComponentModel.single.INModel;
import com.example.deni.logicComponentModel.single.OUTModel;
import com.example.deni.logicComponentModel.single.specific.ANDModel;
import com.example.deni.logicComponentModel.single.specific.NOTModel;
import com.example.deni.logicComponentModel.single.specific.ORModel;
import com.example.deni.logicComponentView.BasicComponentView;
import com.example.deni.logicComponentView.INView;
import com.example.deni.logicComponentView.OUTView;
import com.example.deni.logicComponentView.logicViewComponentsFactory.BasicComponent;
import com.example.deni.logicComponentView.logicViewComponentsFactory.BasicComponentFactory;
import com.example.deni.logicComponentView.specific.ANDView;
import com.example.deni.logicComponentView.specific.NOTView;
import com.example.deni.logicComponentView.specific.ORView;
import com.example.deni.logicViewScheme.SchemeContext;
import com.example.deni.logicViewScheme.SchemeView;

import java.util.ArrayList;
import java.util.List;

public class SchemeFragment extends Fragment {
    public static SchemeFragment newInstance(float width, float height, ArrayList<String> netList){
        Bundle args = new Bundle();
        args.putFloat(EXTRA_SCHEME_DIM_X, width);
        args.putFloat(EXTRA_SCHEME_DIM_Y, height);
        args.putStringArrayList(NET_LIST, netList);

        SchemeFragment fragment = new SchemeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    /**String constants*/
    public static final String NET_LIST =
            "com.example.deni.logic.SchemeFragment.netList";
    public static final String EXTRA_SCHEME_DIM_X =
            "com.example.deni.logic.SchemeFragment.X";
    public static final String EXTRA_SCHEME_DIM_Y =
            "com.example.deni.logic.SchemeFragment.Y";
    public static final String SOURCE = "MENU";

    /**DEBUG TAGS*/
    private static final String TAG = "Debug";

    /**Should become enumeration eventually*/
    private static final int NEW = 0;
    private static final int SAVE = 1;
    private static final int LOAD = 2;

    /**GUI elements*/
    private DrawerLayout mDrawerLayout;
    private ListView mLeftDrawer;
    private ListView mRightDrawer;
    private String[] mLeftDrawerMenu;

    /**Drawables for mRightDrawerMenu*/
    private ArrayList<View> mRightDrawerMenu;

    /**Net list*/
    private ArrayList<String> mNetList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SchemeContext.width = getArguments().getFloat(SchemeFragment.EXTRA_SCHEME_DIM_X);
        SchemeContext.height = getArguments().getFloat(SchemeFragment.EXTRA_SCHEME_DIM_Y);
        mNetList = getArguments().getStringArrayList(NET_LIST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_scheme, container, false);

        mLeftDrawerMenu = getResources().getStringArray(R.array.left_drawer_menu);
        mLeftDrawer = (ListView)v.findViewById(R.id.left_drawer);
        mDrawerLayout = (DrawerLayout)v.findViewById(R.id.drawer_layout);

        mRightDrawer = (ListView)v.findViewById(R.id.right_drawer);
        ViewGroup.LayoutParams lp = mRightDrawer.getLayoutParams();
        lp.width = BasicComponentDefault.N_WIDTH.getValue() * Constants.SCHEME_OFFSET.getDPValue(getActivity());
        mRightDrawer.setLayoutParams(lp);

//        LEFT DRAWER
        mLeftDrawer.setAdapter(new ArrayAdapter<>(
                getActivity(), R.layout.left_drawer_menu_item, mLeftDrawerMenu
        ));
        mLeftDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == NEW) {
                    Log.d(TAG, "NEW"); //TODO : NEW EVENT
                } else if (position == SAVE) {
                    Log.d(TAG, "SAVE"); //TODO : SAVE EVENT
                } else if (position == LOAD) {
                    Log.d(TAG, "LOAD"); //TODO : LOAD EVENT
                }
            }
        });

        BasicComponentFactory cf = new BasicComponentFactory();
        mRightDrawerMenu = new ArrayList<>();
        mRightDrawerMenu.add(cf.create(BasicComponent.IN, getActivity()));
        mRightDrawerMenu.add(cf.create(BasicComponent.OUT, getActivity()));
        mRightDrawerMenu.add(cf.create(BasicComponent.NOT, getActivity()));
        mRightDrawerMenu.add(cf.create(BasicComponent.AND, getActivity()));
        mRightDrawerMenu.add(cf.create(BasicComponent.OR, getActivity()));
        mRightDrawerMenu.add(cf.create(BasicComponent.NAND, getActivity()));
        mRightDrawerMenu.add(cf.create(BasicComponent.NOR, getActivity()));
        mRightDrawerMenu.add(cf.create(BasicComponent.XOR, getActivity()));
        mRightDrawer.setAdapter(new ViewAdapter(mRightDrawerMenu));
        mRightDrawer.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ClipData.Item item = new ClipData.Item(SOURCE);
                ClipData dragData = new ClipData(
                        SOURCE,
                        new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
                        item
                );

                View.DragShadowBuilder shadow = new View.DragShadowBuilder(view);

                //                        View is passed incorrectly, correct when you learn how to implement Parcelable
                view.startDrag(
                        dragData,
                        shadow,
                        view,
                        0
                );
                mDrawerLayout.closeDrawer(mRightDrawer);
                return true;
            }

        });

        SchemeView schemeView = ((SchemeView) v.findViewById(R.id.scheme_view));
//        ANDView first = new ANDView(new ANDModel(getActivity(), new PointF(0, 0)));
//        ANDView second = new ANDView(new ANDModel(getActivity(), new PointF(20 * offset, 0)));
//        schemeView.completeAddView(first);
//        schemeView.completeAddView(second);
//        Connect.getInstance().connectComponents(
//                first.getComponent().getOutputGate(0),
//                second.getComponent().getInputGate(0)
//        );
        if (mNetList != null){
            Log.d("wazoo", mNetList.toString());
            generator(schemeView);
        }

//        ViewGroup.LayoutParams lpSV = schemeView.getLayoutParams();
//        lpSV.height = (int)SchemeContext.height;
//        lpSV.width = (int)SchemeContext.width;
//        schemeView.setLayoutParams(lpSV);

        return v;
    }

    private void generator(SchemeView schemeView){
        float offset = Constants.SCHEME_OFFSET.getDPValue(getActivity());
        List<BasicComponentView> inputs = new ArrayList<>();
        List<BasicComponentView> notGates = new ArrayList<>();
        List<BasicComponentView> andGates = new ArrayList<>();
        List<BasicComponentView> orGates = new ArrayList<>();
        int mode = 0;
        float xPosition = 5 * offset;
        xPosition -= 20 * offset;
        float yPosition = 5 * offset;

        for(String line : mNetList){
            if (line.matches("^[vnao](.*)")){
                mode++;
                yPosition = 5 * offset;
                xPosition += 20 * offset;
                continue;
            }
            switch (mode){
                case 1:
                    if (line.equals("")) { break; }
                    INView in = new INView(new INModel(getActivity(), new PointF(xPosition, yPosition)));
                    schemeView.completeAddView(in);
                    inputs.add(in);
                    yPosition += in.getComponent().getHeight() + 2 * offset;
                    break;
                case 2:
                    if (line.equals("")) {
                        Log.d("wazoo", "brokenNOT");
                        xPosition -= 20 * offset;
                        break;
                    }
                    NOTView not = new NOTView(new NOTModel(getActivity(), new PointF(xPosition, yPosition)));
                    schemeView.completeAddView(not);
                    notGates.add(not);
                    int inputIndex = charToInt(line.charAt(0));
                    connector(inputs.get(inputIndex), not, 0);
                    yPosition += not.getComponent().getHeight() + 2 * offset;
                    break;
                case 3:
                    if (line.equals("")) { Log.d("wazoo", "brokenAND"); break; }
                    String[] parts = line.split("\t");
                    if (parts.length == 2) {
                        int index;
                        if (parts[0].charAt(0) == 'n') {
                            index = charToInt(parts[0].charAt(1));
                            andGates.add(notGates.get(index));
                        } else {
                            index = charToInt(parts[0].charAt(0));
                            andGates.add(inputs.get(index));
                        }
                        break;
                    }

                    ANDView and = new ANDView(new ANDModel(getActivity(), new PointF(xPosition, yPosition)));
                    schemeView.completeAddView(and);
                    andGates.add(and);
                    int input = -1;
                    for (int i = 0; i < parts.length - 1; i++){
                        int index;
                        input++;
                        and.setGateCount(input + 1, 1);
                        if (parts[i].charAt(0) == 'n'){
                            index = charToInt(parts[i].charAt(1));
                            connector(notGates.get(index), and, input);
                        } else {
                            index = charToInt(parts[i].charAt(0));
                            connector(inputs.get(index), and, input);
                        }
                    }
                    yPosition += and.getComponent().getHeight() + 2 * offset;
                    break;
                case 4:
                    if (line.equals("")) { Log.d("wazoo", "brokenOR"); break; }
                    parts = line.split("\t");
                    ORView or = new ORView(new ORModel(getActivity(), new PointF(xPosition, yPosition)));
                    schemeView.completeAddView(or);
                    orGates.add(or);
                    input = -1;
                    for (int i = 0; i < parts.length - 1; i++){
                        input++;
                        or.setGateCount(input + 1, 1);
                        int index = charToInt(parts[i].charAt(0));
                        connector(andGates.get(index), or, input);
                    }
                    yPosition += or.getComponent().getHeight() + 2 * offset;
                    break;
            }
        }
        xPosition += 6 * offset;
        yPosition = 5 * offset;
        for (BasicComponentView gate : orGates){
            OUTView out = new OUTView(new OUTModel(getActivity(), new PointF(xPosition, yPosition)));
            schemeView.addView(out);
            connector(gate, out, 0);
            yPosition += out.getComponent().getHeight() + 2 * offset;
        }
    }

    private int charToInt(char c){
        return (int) c - 48;
    }

    private void connector(BasicComponentView left, BasicComponentView right, int rightIndex){
        Connect.getInstance().connectComponents(
                left.getComponent().getOutputGate(0),
                right.getComponent().getInputGate(rightIndex)
        );
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Connect.getInstance().wipe();
    }

    /**
     * Adapter made for RightDrawer.
     */
    private class ViewAdapter extends BaseAdapter{
        private List<View> mViews;

        public ViewAdapter(List<View> views){
            mViews = views;
        }

        @Override
        public int getCount() {
            return mViews.size();
        }

        @Override
        public Object getItem(int position) {
            return mViews.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return mViews.get(position);
        }
    }
}
