package com.example.commons.annoation;


import com.example.commons.entity.EntitySqlFactory;
import com.example.commons.utils.ClassTools;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;


@Aspect
@Component
@Slf4j
public class EntitySqlScanRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {
    private ResourceLoader resourceLoader;

    public EntitySqlScanRegistrar() {
        // 不做任何操作
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EntitySqlScan.class.getName()));

        //扫描的包名
        String[] basePackages = annoAttrs.getStringArray("basePackages");
        //扫描的类
        Class<?>[] basePackageClasses= annoAttrs.getClassArray("basePackageClasses");
        //
        Set<Class<?>> clazzList = new LinkedHashSet<>();
        //遍历原始包名，判断是否有模糊查早，找到所有顶级包
        if(basePackages!=null&&basePackages.length>0){
            for (String p : basePackages){
                //if(p.indexOf("*")>=0){
               // }
                Set<Class<?>> set = ClassTools.getClasses(p);
                clazzList.addAll(set);
            }
        }

        //添加具体类
        for(Class c:basePackageClasses){
            clazzList.add(c);
        }

        //遍历所有类，生成数据表信息
        clazzList.forEach(aClass -> EntitySqlFactory.addClassMap(aClass));

        log.info("------------");
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}

