package com.example.buuktu.adapters;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.utils.EfectsUtils;
import com.example.buuktu.views.CreateEditScenariokie;
import com.example.buuktu.R;
import com.example.buuktu.dialogs.DeleteGeneralDialog;
import com.example.buuktu.models.ScenariokieModel;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.utils.NavigationUtils;
import com.example.buuktu.views.MainActivity;
import com.example.buuktu.views.Scenariokie;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ScenariokiesUserPreviewAdapter extends RecyclerView.Adapter<ScenariokiesUserPreviewAdapter.ViewHolder> {


    private final ArrayList<ScenariokieModel> dataSet;

    private final MainActivity context;
    private final FragmentManager fragmentManager;
    private final String mode;
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView iv_scenariokie_preview_worldkie;
        private final ImageView iv_scenariokie_private_preview;
        private final TextView tv_scenariokie_preview_worldkie;
        private final TextView tv_scenariokie_preview_draft;
        final CardView cardView;

        public ViewHolder(View view) {
            super(view);
            iv_scenariokie_preview_worldkie =  view.findViewById(R.id.iv_scenariokie_preview_worldkie);
            tv_scenariokie_preview_worldkie = view.findViewById(R.id.tv_scenariokie_preview_worldkie);
            iv_scenariokie_private_preview = view.findViewById(R.id.iv_scenariokie_private_preview);
            tv_scenariokie_preview_draft = view.findViewById(R.id.tv_scenariokie_preview_draft);
            cardView = view.findViewById(R.id.cv_scenariokie_preview);
        }

        public ImageView getIv_scenariokie_preview_worldkie() {
            return iv_scenariokie_preview_worldkie;
        }

        public TextView getTv_scenariokie_preview_worldkie() {
            return tv_scenariokie_preview_worldkie;
        }

        public TextView getTv_scenariokie_preview_draft() {
            return tv_scenariokie_preview_draft;
        }

        public ImageView getIv_scenariokie_private_preview() {
            return iv_scenariokie_private_preview;
        }

        public CardView getCardView() {
            return cardView;
        }
    }

    public ScenariokiesUserPreviewAdapter(ArrayList<ScenariokieModel> dataSet, MainActivity ctx, FragmentManager fragmentManager, String mode) {
        this.dataSet = dataSet;
        this.context = ctx;
        this.fragmentManager = fragmentManager;
        this.mode=mode;
    }

    
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

           View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.scenariokie_list_layout_preview, viewGroup, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ScenariokiesUserPreviewAdapter.ViewHolder holder, int position) {
        ScenariokieModel scenariokieModel = dataSet.get(position);
        holder.getTv_scenariokie_preview_worldkie().setText(scenariokieModel.getName());
        if(!scenariokieModel.isDraft()){
            holder.getTv_scenariokie_preview_draft().setVisibility(View.INVISIBLE);
        }
        if(!scenariokieModel
                .isScenariokie_private()){
            holder.getIv_scenariokie_private_preview().setVisibility(View.INVISIBLE);
        }
        holder.getCardView().setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("mode",mode);
            bundle.putString("UID",scenariokieModel.getUID());
            bundle.putString("UID_AUTHOR",scenariokieModel.getAUTHOR_UID());
            bundle.putString("UID_WORLDKIE",scenariokieModel.getWORDLKIE_UID());
            NavigationUtils.goNewFragmentWithBundle(bundle,fragmentManager,new Scenariokie());
        });
        if(mode.equals("self")) {
            holder.getCardView().setOnLongClickListener(v -> {
                View popupView = LayoutInflater.from(context).inflate(R.layout.menu_popup, null);
                PopupWindow popupWindow = new PopupWindow(popupView,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        true);

                popupWindow.showAsDropDown(holder.getCardView(), 0, -50);

                popupView.findViewById(R.id.bt_edit_item).setOnClickListener(view -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("scenariokie_id", scenariokieModel.getUID());
                    NavigationUtils.goNewFragmentWithBundle(bundle, fragmentManager, new CreateEditScenariokie());
                    popupWindow.dismiss();
                });

                popupView.findViewById(R.id.bt_del_item).setOnClickListener(view2 -> {
                    DeleteGeneralDialog deleteGeneralDialog = new DeleteGeneralDialog(context, "scenariokie", scenariokieModel.getUID());
                    deleteGeneralDialog.show();
                    popupWindow.dismiss();
                });
                return true;
            });
        }
            if (scenariokieModel.isPhoto_default()) {
                int resId = context.getResources().getIdentifier(scenariokieModel.getPhoto_id(), "mipmap", context.getPackageName());

                if (resId != 0) {
                    Drawable drawable = ContextCompat.getDrawable(context, resId);

                    holder.getIv_scenariokie_preview_worldkie().setImageDrawable(drawable);
                        DrawableUtils.personalizarImagenCuadradoButton(context,115/6,7,R.color.brownMaroon,drawable, holder.getIv_scenariokie_preview_worldkie());
                    holder.getIv_scenariokie_preview_worldkie().setVisibility(View.VISIBLE);
                }
        } else {
            context.getFirebaseStorageScenariokies().getReference(scenariokieModel.getUID()).listAll().addOnSuccessListener(listResult -> {
                for (StorageReference item : listResult.getItems()) {
                    if (item.getName().startsWith("cover")) {
                        item.getDownloadUrl().addOnSuccessListener(uri -> {
                                        DrawableUtils.personalizarImagenCuadradoButton(context ,150/7,7,R.color.brownMaroon,uri, holder.getIv_scenariokie_preview_worldkie());
                                holder.getIv_scenariokie_preview_worldkie().setVisibility(View.VISIBLE);
                        EfectsUtils.startCircularReveal(context, uri, holder.getIv_scenariokie_preview_worldkie());
                        });

                }
                }
            });
            }
    }
    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}