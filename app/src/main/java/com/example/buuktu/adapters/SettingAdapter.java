package com.example.buuktu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.buuktu.R;
import com.example.buuktu.dialogs.CreateEditGeneralDialog;
import com.example.buuktu.dialogs.EditNamePronounsUserDialog;
import com.example.buuktu.dialogs.EditPasswordUserDialog;
import com.example.buuktu.models.SettingModel;
import com.example.buuktu.utils.CheckUtil;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.ViewHolder> implements View.OnClickListener {

    @Override
    public void onClick(View v) {

    }
    private ArrayList<SettingModel> dataSet;
    private Context context;
    FirebaseFirestore db;
    CollectionReference collectionReference;
    DocumentReference documentReference;
    FirebaseAuth firebaseAuth;
    String UID;
    TextView tv_edittext_general_dialog_error;
    TextInputEditText et_namepronouns;
    TextInputLayout et_namepronounsFull;
    EditNamePronounsUserDialog editNamePronounsUserDialog;
    CreateEditGeneralDialog dialogCreateEdit;
    public class ViewHolder extends RecyclerView.ViewHolder {
        String lastName="", lastPronouns="",lastEmail="";
        private TextView tv_name_setting_profile,tv_value_setting_profile;
        private CardView card_view_setting_list_profile;
        //private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance("gs://buuk-tu-worldkies");
        //private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        public ViewHolder(View view) {
            super(view);
            tv_value_setting_profile =  view.findViewById(R.id.tv_value_setting_profile);
            card_view_setting_list_profile= view.findViewById(R.id.card_view_setting_list_profile);
            tv_name_setting_profile = view.findViewById(R.id.tv_name_setting_profile);
        }

        public CardView getCard_view_setting_list_profile() {
            return card_view_setting_list_profile;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getLastPronouns() {
            return lastPronouns;
        }

        public void setLastPronouns(String lastPronouns) {
            this.lastPronouns = lastPronouns;
        }

        public TextView getTv_value_setting_profile() {
            return tv_value_setting_profile;
        }

        public TextView getTv_name_setting_profile() {
            return tv_name_setting_profile;
        }
    }

    //Constructor donde pasamos la lista de productos y el contexto
    public SettingAdapter(ArrayList<SettingModel> dataSet, Context ctx, String UID) {
        this.dataSet = dataSet;
        this.context = ctx;
        firebaseAuth = FirebaseAuth.getInstance();
        this.UID = UID;
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Users");
        documentReference = collectionReference.document(UID);
        dialogCreateEdit = new CreateEditGeneralDialog(context);
    }


    //Se llama cada vez que se hace scroll en la pantalla y los elementos desaparecen y aparecen
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        //Creamos la vista de cada item a partir de nuestro layout
           View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.settings_profile_list_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingAdapter.ViewHolder holder, int position) {
        String name = dataSet.get(holder.getAdapterPosition()).getName();
        String value = dataSet.get(holder.getAdapterPosition()).getValue();
        switch (name.toLowerCase()){
            case "nombre":
                if(!holder.getLastName().equals(value)){
                    holder.getTv_value_setting_profile().setText(value);
                }
                break;
            case "pronombres":
                if(!holder.getLastPronouns().equals(value)){
                    holder.getTv_value_setting_profile().setText(value);
                }
                break;
            case "correo electronico":
                if(!holder.getLastPronouns().equals(value)){
                    holder.getTv_value_setting_profile().setText(value);
                }
                break;
        }
        holder.getTv_name_setting_profile().setText(name);

        holder.getTv_value_setting_profile().setText(value);
         holder.getCard_view_setting_list_profile().setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 switch (name.toLowerCase()) {
                     case "nombre":
                         editNamePronounsUserDialog = new EditNamePronounsUserDialog(v.getContext(),"Nombre",value);
                     editNamePronounsUserDialog.setOnDialogClickListener(new EditNamePronounsUserDialog.OnDialogEditClickListener() {
                         @Override
                         public void onAccept() {
                            editName(editNamePronounsUserDialog);
                         }

                         @Override
                         public void onCancel() {
                            editNamePronounsUserDialog.dismiss();
                         }
                     });
                     editNamePronounsUserDialog.show();
                     Toast.makeText(context, "Perfil", Toast.LENGTH_SHORT).show();
                     break;
                     case "pronombres":
                         EditNamePronounsUserDialog editNamePronounsUserDialogPronouns = new EditNamePronounsUserDialog(v.getContext(),"Pronombres",value);
                         editNamePronounsUserDialogPronouns.setOnDialogClickListener(new EditNamePronounsUserDialog.OnDialogEditClickListener() {
                             @Override
                             public void onAccept() {
                                editPronouns(editNamePronounsUserDialogPronouns);
                             }

                             @Override
                             public void onCancel() {
                                 editNamePronounsUserDialogPronouns.dismiss();
                             }
                         });
                         editNamePronounsUserDialogPronouns.show();
                     Toast.makeText(context, "Cuenta", Toast.LENGTH_SHORT).show();
                     break;
                     case "correo electronico":
                         EditNamePronounsUserDialog editNamePronounsUserDialogEmail = new EditNamePronounsUserDialog(v.getContext(),"Correo electronico",value);
                         editNamePronounsUserDialogEmail.setOnDialogClickListener(new EditNamePronounsUserDialog.OnDialogEditClickListener() {
                             @Override
                             public void onAccept() {
                                editEmail(editNamePronounsUserDialogEmail);
                             }

                             @Override
                             public void onCancel() {
                                 editNamePronounsUserDialogEmail.dismiss();
                             }
                         });
                         editNamePronounsUserDialogEmail.show();
                     Toast.makeText(context, "Correo electronico", Toast.LENGTH_SHORT).show();
break;
                     case "password":
                     EditPasswordUserDialog editPasswordUserDialog = new EditPasswordUserDialog(context);
                     editPasswordUserDialog.setOnDialogClickListener(new EditPasswordUserDialog.OnDialogEditClickListener() {
                         @Override
                         public void onAccept() {
                            editPassword(editPasswordUserDialog);
                         }

                         @Override
                         public void onCancel() {
                             editPasswordUserDialog.dismiss();
                         }
                     });
                     editPasswordUserDialog.show();
                     break;
                 }
             }
         });
    }
        private void editEmail(EditNamePronounsUserDialog dialog) {
            et_namepronouns = dialog.findViewById(R.id.et_namepronouns);

            tv_edittext_general_dialog_error = dialog.findViewById(R.id.tv_edittext_general_dialog_error);
            tv_edittext_general_dialog_error.setText("");
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String oldEmail = user.getEmail();
            String newEmail = et_namepronouns.getText().toString();
            if (!newEmail.equals("") && !oldEmail.equalsIgnoreCase(newEmail)) {
                dialogCreateEdit.show();
                LottieAnimationView animationViewCreateEdit = dialogCreateEdit.findViewById(R.id.anim_create_edit);
                animationViewCreateEdit.setAnimation(R.raw.reading_anim);
                animationViewCreateEdit.playAnimation();
                Completable.timer(2, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                                    user.updateEmail(newEmail).addOnCompleteListener(updateTask -> {
                                        if (updateTask.isSuccessful()) {
                                            animationViewCreateEdit.setAnimation(R.raw.success_anim);
                                            animationViewCreateEdit.playAnimation();
                                            Completable.timer(2, TimeUnit.SECONDS)
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(() -> {
                                                        dialogCreateEdit.dismiss();
                                                    });
                                            Toast.makeText(dialog.getContext(), "Contraseña cambiada con éxito", Toast.LENGTH_SHORT).show();
                                        } else {
                                            animationViewCreateEdit.setAnimation(R.raw.fail_anim);
                                            animationViewCreateEdit.playAnimation();
                                            Completable.timer(2, TimeUnit.SECONDS)
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(() -> {
                                                        dialogCreateEdit.dismiss();
                                                    });
                                            Toast.makeText(dialog.getContext(), "Error al cambiar la contraseña", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                        );
            }
        }
    private void editName(EditNamePronounsUserDialog dialog){
        et_namepronouns = dialog.findViewById(R.id.et_namepronouns);
        et_namepronounsFull = dialog.findViewById(R.id.et_namepronounsFull);
        if(CheckUtil.handlerCheckName(context,et_namepronouns,et_namepronounsFull)) {
            dialogCreateEdit.show();
            LottieAnimationView animationViewCreateEdit = dialogCreateEdit.findViewById(R.id.anim_create_edit);
            animationViewCreateEdit.setAnimation(R.raw.reading_anim);
            animationViewCreateEdit.playAnimation();
            Completable.timer(2, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
            Map<String, Object> worldkieData = new HashMap<>();
            worldkieData.put("name", et_namepronouns.getText().toString());
            documentReference.update(worldkieData).addOnCompleteListener(updateTask -> {
                    if (updateTask.isSuccessful()) {
                        animationViewCreateEdit.setAnimation(R.raw.success_anim);
                        animationViewCreateEdit.playAnimation();
                        Completable.timer(2, TimeUnit.SECONDS)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {
                                    dialogCreateEdit.dismiss();
                                });
                        Toast.makeText(dialog.getContext(), "Contraseña cambiada con éxito", Toast.LENGTH_SHORT).show();
                    } else {
                        animationViewCreateEdit.setAnimation(R.raw.fail_anim);
                        animationViewCreateEdit.playAnimation();
                        Completable.timer(5, TimeUnit.SECONDS)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {
                                    dialogCreateEdit.dismiss();
                                });
                        Toast.makeText(dialog.getContext(), "Error al cambiar la contraseña", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }
        }
    private void editPronouns(EditNamePronounsUserDialog dialog){
        et_namepronouns = dialog.findViewById(R.id.et_namepronouns);
        et_namepronounsFull = dialog.findViewById(R.id.et_namepronounsFull);
        String newPronouns = et_namepronouns.getText().toString();
        if(CheckUtil.handlerCheckPronouns(context,et_namepronouns,et_namepronounsFull)) {
            dialogCreateEdit.show();
            LottieAnimationView animationViewCreateEdit = dialogCreateEdit.findViewById(R.id.anim_create_edit);
            animationViewCreateEdit.setAnimation(R.raw.reading_anim);
            animationViewCreateEdit.playAnimation();
            Completable.timer(3, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                                Map<String, Object> worldkieData = new HashMap<>();
                                worldkieData.put("pronouns", newPronouns);
                                documentReference.update(worldkieData).addOnCompleteListener(updateTask -> {
                                    if (updateTask.isSuccessful()) {
                                        animationViewCreateEdit.setAnimation(R.raw.success_anim);
                                        animationViewCreateEdit.playAnimation();
                                        Completable.timer(3, TimeUnit.SECONDS)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(() -> {
                                                    dialogCreateEdit.dismiss();
                                                });
                                    } else {
                                        animationViewCreateEdit.setAnimation(R.raw.fail_anim);
                                        animationViewCreateEdit.playAnimation();
                                        Completable.timer(5, TimeUnit.SECONDS)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(() -> {
                                                    dialogCreateEdit.dismiss();
                                                });
                                    }
                                });
                            }
                    );
        }
    }
    private void editPassword(EditPasswordUserDialog dialog) {
        TextView tv_title = dialog.findViewById(R.id.tv_edit_title);
        tv_title.setText("Cuidado");
        TextInputEditText et_oldpassword = dialog.findViewById(R.id.et_oldpassword);
        TextInputEditText et_newPassword = dialog.findViewById(R.id.et_newPassword);
        TextInputEditText et_newPasswordRepeat = dialog.findViewById(R.id.et_newPasswordRepeat);
        //Log.d("SettingAdapter", "Valor recibido: " + dataSet.get(position).getValue());

        //ImageView iv_photo = dialog.findViewById(R.id.iv_photo_del);
        //iv_photo.setImageAlpha(R.mipmap.img_del_characterkie);
        ImageButton ib_close = dialog.findViewById(R.id.ib_close_dialog);
        ImageButton ib_accept = dialog.findViewById(R.id.ib_accept_dialog);
        LottieAnimationView animationView = dialog.findViewById(R.id.anim_edit);

        tv_title.setVisibility(View.GONE);
        // tv_text.setVisibility(View.GONE);
        // iv_photo.setVisibility(View.GONE);
        ib_close.setVisibility(View.GONE);
        ib_accept.setVisibility(View.GONE);
        animationView.setVisibility(View.VISIBLE);
        animationView.setAnimation(R.raw.reading_anim);
        animationView.playAnimation();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String newPassword = et_newPassword.getText().toString();
        String newPasswordRepeat = et_newPasswordRepeat.getText().toString();
       // if(CheckUtil.handlerCheckPassword(context,et_newPassword,))
        if (newPassword.equals(newPasswordRepeat)) {
            String email = user.getEmail();

            String oldPassword = et_oldpassword.getText().toString();
            AuthCredential credential = EmailAuthProvider.getCredential(email, oldPassword);

            user.reauthenticate(credential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Si la contraseña actual es correcta, cambiamos la contraseña
                    user.updatePassword(newPassword).addOnCompleteListener(updateTask -> {
                        if (updateTask.isSuccessful()) {
                            animationView.setAnimation(R.raw.success_anim);
                            animationView.playAnimation();
                            Completable.timer(5, TimeUnit.SECONDS)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(() -> {
                                        animationView.setVisibility(View.GONE);
                                        dialog.dismiss();
                                    });
                            Toast.makeText(dialog.getContext(), "Contraseña cambiada con éxito", Toast.LENGTH_SHORT).show();
                        } else {
                            animationView.setAnimation(R.raw.fail_anim);
                            animationView.playAnimation();
                            Completable.timer(5, TimeUnit.SECONDS)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(() -> {
                                        animationView.setVisibility(View.GONE);
                                        dialog.dismiss();
                                    });
                            Toast.makeText(dialog.getContext(), "Error al cambiar la contraseña", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    animationView.setAnimation(R.raw.fail_anim);
                    animationView.playAnimation();
                    Completable.timer(5, TimeUnit.SECONDS)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(() -> {
                                animationView.setVisibility(View.GONE);
                                dialog.dismiss();
                            });
                    Toast.makeText(dialog.getContext(), "Contraseña actual incorrecta", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Devolvemos el numero de items de nuestro arraylist, lo invoca automaticamente el layout manager
    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}