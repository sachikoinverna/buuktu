package com.example.buuktu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.buuktu.adapters.NoteAdapter;
import com.example.buuktu.models.CardItem;
import com.example.buuktu.models.NoteItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Notes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Notes extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView rc_all_notes_adapter;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    NoteAdapter noteAdapter;
    public Notes() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Notes.
     */
    // TODO: Rename and change types and number of parameters
    public static Notes newInstance(String param1, String param2) {
        Notes fragment = new Notes();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_notes, container, false);
        rc_all_notes_adapter = view.findViewById(R.id.rc_all_notes_adapter);
        rc_all_notes_adapter.setHasFixedSize(true);
        staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        rc_all_notes_adapter.setLayoutManager(staggeredGridLayoutManager);


        List<NoteItem> items = new ArrayList<>();
        items.add(new NoteItem( "El sol comenzaba a ocultarse en el horizonte, tiñendo el cielo de tonos naranjas y rosados.\n" +
                "Una brisa suave recorría el campo, moviendo las hojas de los árboles con delicadeza.\n" +
                "En la distancia, el canto de los pájaros anunciaba la llegada de la noche.\n" +
                "Las luces de la ciudad empezaban a encenderse poco a poco, iluminando las calles.\n" +
                "Un gato caminaba sigilosamente por la acera, en busca de un rincón donde descansar.\n" +
                "Los niños jugaban en el parque, riendo sin preocuparse por el paso del tiempo.\n" +
                "Desde una ventana abierta, se escuchaba el sonido de una melodía tranquila.\n" +
                "Las estrellas comenzaron a brillar, una a una, sobre el cielo despejado.\n" +
                "El aroma a café recién hecho flotaba en el aire, invitando al descanso.\n" +
                "En ese instante, el mundo parecía estar en perfecta armonía.","Elemento 1"));
        items.add(new NoteItem( "El sol comenzaba a ocultarse en el horizonte, tiñendo el cielo de tonos naranjas y rosados.\n" +
                "Una brisa suave recorría el campo, moviendo las hojas de los árboles con delicadeza.\n" +
                "En la distancia, el canto de los pájaros anunciaba la llegada de la noche.\n" +
                "Las luces de la ciudad empezaban a encenderse poco a poco, iluminando las calles.\n" +
                "Un gato caminaba sigilosamente por la acera, en busca de un rincón donde descansar.\n" +
                "Los niños jugaban en el parque, riendo sin preocuparse por el paso del tiempo.\n" +
                "Desde una ventana abierta, se escuchaba el sonido de una melodía tranquila.\n" +
                "Las estrellas comenzaron a brillar, una a una, sobre el cielo despejado.\n" +
                "El aroma a café recién hecho flotaba en el aire, invitando al descanso.\n" +
                "En ese instante, el mundo parecía estar en perfecta armonía.","Elemento 2"));
        items.add(new NoteItem( "El sol comenzaba a ocultarse en el horizonte, tiñendo el cielo de tonos naranjas y rosados.\n" +
                "Una brisa suave recorría el campo, moviendo las hojas de los árboles con delicadeza.\n" +
                "En la distancia, el canto de los pájaros anunciaba la llegada de la noche.\n" +
                "Las luces de la ciudad empezaban a encenderse poco a poco, iluminando las calles.\n" +
                "Un gato caminaba sigilosamente por la acera, en busca de un rincón donde descansar.\n" +
                "Los niños jugaban en el parque, riendo sin preocuparse por el paso del tiempo.\n" +
                "Desde una ventana abierta, se escuchaba el sonido de una melodía tranquila.\n" +
                "Las estrellas comenzaron a brillar, una a una, sobre el cielo despejado.\n" +
                "El aroma a café recién hecho flotaba en el aire, invitando al descanso.\n" +
                "En ese instante, el mundo parecía estar en perfecta armonía.","Elemento 3"));
        items.add(new NoteItem( "El sol comenzaba a ocultarse en el horizonte, tiñendo el cielo de tonos naranjas y rosados.\n" +
                "Una brisa suave recorría el campo, moviendo las hojas de los árboles con delicadeza.\n" +
                "En la distancia, el canto de los pájaros anunciaba la llegada de la noche.\n" +
                "Las luces de la ciudad empezaban a encenderse poco a poco, iluminando las calles.\n" +
                "Un gato caminaba sigilosamente por la acera, en busca de un rincón donde descansar.\n" +
                "Los niños jugaban en el parque, riendo sin preocuparse por el paso del tiempo.\n" +
                "Desde una ventana abierta, se escuchaba el sonido de una melodía tranquila.\n" +
                "Las estrellas comenzaron a brillar, una a una, sobre el cielo despejado.\n" +
                "El aroma a café recién hecho flotaba en el aire, invitando al descanso.\n" +
                "En ese instante, el mundo parecía estar en perfecta armonía.","Elemento 4"));
        noteAdapter = new NoteAdapter(getContext(), items, new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NoteItem item) {
                // Manejar el clic en la nota (por ejemplo, abrir detalle o mostrar Toast)
            }
        });
        rc_all_notes_adapter.setAdapter(noteAdapter);
        return view;
    }
}