package com.example.buuktu.adapters;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.views.CharacterkieView;
import com.example.buuktu.R;
import com.example.buuktu.models.ScenariokieModel;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.utils.NavigationUtils;
import com.example.buuktu.views.MainActivity;
import com.example.buuktu.views.Scenariokie;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ScenariokieSearchAdapter extends RecyclerView.Adapter<ScenariokieSearchAdapter.ViewHolder>{

    private final ArrayList<ScenariokieModel> dataSet;
    private final FragmentManager fragmentManager;

    private final MainActivity context;
    private String mode;
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView iv_scenariokie_photo_search;
        private final ImageView iv_characterkie_private_search;
        final MaterialCardView cv_scenariokie_search;
        final TextView tv_characterkie_name_search;
        final TextView tv_characterkie_username_search;
        public ViewHolder(View view) {
            super(view);
            tv_characterkie_username_search = view.findViewById(R.id.tv_scenariokie_username_search);
            tv_characterkie_name_search =  view.findViewById(R.id.tv_scenariokie_name_search);
            iv_scenariokie_photo_search = view.findViewById(R.id.iv_scenariokie_photo_search);
            cv_scenariokie_search = view.findViewById(R.id.cv_scenariokie_search);
            iv_characterkie_private_search = view.findViewById(R.id.iv_scenariokie_private_search);
        }

        public TextView getTv_characterkie_username_search() {
            return tv_characterkie_username_search;
        }

        public TextView getTv_characterkie_name_search() {
            return tv_characterkie_name_search;
        }

        public MaterialCardView getCv_scenariokie_search() {
            return cv_scenariokie_search;
        }

        public ImageView getIv_scenariokie_photo_search() {
            return iv_scenariokie_photo_search;
        }
        public ImageView getIv_characterkie_private_search() {
            return iv_characterkie_private_search;
        }
    }
    public ScenariokieSearchAdapter(ArrayList<ScenariokieModel> dataSet, MainActivity ctx, FragmentManager fragmentManager, String mode) {
        this.dataSet = dataSet;
        this.context = ctx;
        this.fragmentManager = fragmentManager;
        this.mode=mode;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.scenariokie_list_layout_search, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ScenariokieModel scenariokieModel = dataSet.get(position);
        holder.getTv_characterkie_name_search().setText(scenariokieModel.getName());
        if(scenariokieModel.isScenariokie_private()){
            holder.getIv_characterkie_private_search().setVisibility(View.INVISIBLE);
        }
        context.getCollectionUsers().document(scenariokieModel.getAUTHOR_UID()).addSnapshotListener((documentSnapshot, e) -> {
            if (e != null) return;

            if (documentSnapshot != null) {
                holder.getTv_characterkie_username_search().setText(documentSnapshot.getString("username"));
            }
        });
        holder.getCv_scenariokie_search().setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("mode",mode);
            bundle.putString("UID",scenariokieModel.getUID());
            bundle.putString("UID_AUTHOR",scenariokieModel.getAUTHOR_UID());
            bundle.putString("UID_WORLDKIE",scenariokieModel.getWORDLKIE_UID());
            NavigationUtils.goNewFragmentWithBundle(bundle,fragmentManager,new Scenariokie());

        });
        if (scenariokieModel.isPhoto_default()) {
            int resId = context.getResources().getIdentifier(scenariokieModel.getPhoto_id(), "mipmap", context.getPackageName());

            if (resId != 0) {
                Drawable drawable = ContextCompat.getDrawable(context, resId);
                holder.getIv_scenariokie_photo_search().setImageDrawable(drawable);
                    DrawableUtils.personalizarImagenCuadradoButton(context,115/6,7,R.color.brownMaroon,drawable, holder.getIv_scenariokie_photo_search());
            }
        } else {
            context.getFirebaseStorageScenariokies().getReference(scenariokieModel.getUID()).listAll().addOnSuccessListener(listResult -> {
                for (StorageReference item : listResult.getItems()) {
                    if (item.getName().startsWith("cover")) {
                        item.getDownloadUrl().addOnSuccessListener(uri -> DrawableUtils.personalizarImagenCuadradoButton(context,115/6,7,R.color.brownMaroon,uri, holder.getIv_scenariokie_photo_search()));
                        break;
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
