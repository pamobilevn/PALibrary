package co.pamobile.pacore.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import co.pamobile.pacore.R;
import co.pamobile.pacore.Utilities.Utils;

public class DialogExit extends Dialog {

    //@BindView(R.id.btnCancel)
    Button btnCancel;
    //@BindView(R.id.btnExit)
    Button btnExit;
    //@BindView(R.id.imgEvent)
    ImageView imgEvent;
    //@BindView(R.id.rvFeatureApps)
    RecyclerView rvFeatureApps;
    FeatureBanner featureBanner = new FeatureBanner();


    List<AppItem> featureItems = new ArrayList<>();
    Context mContext;

    public DialogExit(@NonNull Context context, List<AppItem> featureItems, FeatureBanner featureBanner) {
        super(context);
        this.mContext = context;
        this.featureItems = featureItems;
        this.featureBanner  = featureBanner;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.layout_dialog_exit,null);
        setContentView(R.layout.layout_dialog_exit);
        getWindow().setLayout(width, height);
        //ButterKnife.bind(this);
        btnCancel = contentView.findViewById(R.id.btnCancel);
        btnExit = contentView.findViewById(R.id.btnExit);
        imgEvent = contentView.findViewById(R.id.imgEvent);
        rvFeatureApps = contentView.findViewById(R.id.rvFeatureApps);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) mContext;
                activity.finish();
            }
        });


        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
        FeatureAppAdapter adapter = new FeatureAppAdapter(getContext(), featureItems);
        rvFeatureApps.setLayoutManager(layoutManager);
        rvFeatureApps.setHasFixedSize(true);
        rvFeatureApps.setAdapter(adapter);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.banner_default)
                .error(R.drawable.banner_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .dontAnimate()
                .dontTransform();

        Glide.with(mContext)
                .load(featureBanner.getUrl())
                .apply(options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                        return false;
                    }
                })
                .into(imgEvent);

        imgEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (featureBanner.getType()){
                    case "google":
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(featureBanner.getDestination()));
                        mContext.startActivity(intent);
                        break;
                    case "facebook":
                        Utils.openFacebookURL((Activity) mContext , featureBanner.getDestination());
                        break;
                    case "youtube":
                        Utils.openYouTubeURL((Activity) mContext , featureBanner.getDestination());
                        break;
                    default:
                        break;
                }
            }
        });

    }

}
