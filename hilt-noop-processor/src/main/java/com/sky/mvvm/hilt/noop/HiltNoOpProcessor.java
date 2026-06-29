package com.sky.mvvm.hilt.noop;

import java.util.Collections;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

/**
 * 一个无操作的注解处理器，仅用于声明识别 Hilt 在 KSP 场景下仍注入到 javac 的内部选项，
 * 从而消除 javac 的“以下选项未被任何处理程序识别”警告。
 */
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class HiltNoOpProcessor extends AbstractProcessor {

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.emptySet();
    }

    @Override
    public Set<String> getSupportedOptions() {
        return Set.of(
                "dagger.hilt.internal.useAggregatingRootProcessor",
                "dagger.fastInit",
                "dagger.hilt.android.internal.disableAndroidSuperclassValidation",
                "dagger.hilt.android.internal.projectType"
        );
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return false;
    }
}
