/*
 * $Id$
 *
 * Copyright 2014 Allen D. Ball.  All rights reserved.
 */
package ball.game.scrabble;

import ball.annotation.ServiceProviderFor;
import ball.annotation.processing.AbstractAnnotationProcessor;
import ball.annotation.processing.For;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import static javax.lang.model.element.Modifier.ABSTRACT;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.tools.Diagnostic.Kind.ERROR;

/**
 * {@link Processor} implementation to check {@link Class}es annotated with
 * {@link LetterPremium} or {@link WordPremium}:
 * <ol>
 *   <li value="1">Are an instance of {@link SQ},</li>
 *   <li value="2">Concrete, and</li>
 *   <li value="3">
 *      Are annotated with only one of {@link LetterPremium} or
 *      {@link WordPremium}
 *   </li>
 * </ol>
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
@ServiceProviderFor({ Processor.class })
@For({ LetterPremium.class, WordPremium.class })
public class PremiumProcessor extends AbstractAnnotationProcessor {
    private TypeElement supertype = null;

    /**
     * Sole constructor.
     */
    public PremiumProcessor() { super(); }

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        supertype = getTypeElementFor(SQ.class);
    }

    @Override
    public void process(RoundEnvironment roundEnv,
                        TypeElement annotation,
                        Element element) throws Exception {
        switch (element.getKind()) {
        case CLASS:
            if (isAssignable(element, supertype)) {
                if (! element.getModifiers().contains(ABSTRACT)) {
                    if (hasPublicNoArgumentConstructor(element)) {
                        HashSet<TypeElement> set = new HashSet<TypeElement>();

                        for (Class<? extends Annotation> type :
                                 getSupportedAnnotationTypeList()) {
                            if (element.getAnnotation(type) != null) {
                                set.add(getTypeElementFor(type));
                            }
                        }

                        set.remove(annotation);

                        if (! set.isEmpty()) {
                            print(ERROR,
                                  element,
                                  element.getKind() + " annotated with "
                                  + AT + annotation.getSimpleName()
                                  + " but is also annotated with "
                                  + AT + set.iterator().next().getSimpleName());
                        }
                    } else {
                        print(ERROR,
                              element,
                              element.getKind() + " annotated with "
                              + AT + annotation.getSimpleName()
                              + " but does not have a " + PUBLIC
                              + " no-argument constructor");
                    }
                } else {
                    print(ERROR,
                          element,
                          element.getKind() + " annotated with "
                          + AT + annotation.getSimpleName()
                          + " but is " + ABSTRACT);
                }
            } else {
                print(ERROR,
                      element,
                      element.getKind() + " annotated with "
                      + AT + annotation.getSimpleName()
                      + " but is not a subclass of "
                      + supertype.getQualifiedName());
            }
            break;

        default:
            break;
        }
    }
}
