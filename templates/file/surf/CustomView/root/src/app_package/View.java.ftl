package ${packageName};

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;


public class ${className} extends ${parentClassName} {

    public ${className}(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    <#if generateListener>
        public interface ${className}Listener {
        }

        private ${className}Listener listener;

        public void setListener(${className}Listener listener) {
            this.listener = listener;
        }
    </#if>

    <#if screenModelName!=''>
        public void render(${screenModelName} screenModel) {
        }
    </#if>

    private void initView() {
        inflate(getContext(), R.layout.${layoutName}, this);
    }

}
