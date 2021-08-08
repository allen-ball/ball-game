package ball.game.scrabble;
/*-
 * ##########################################################################
 * Game Applications and Utilities
 * $Id$
 * $HeadURL$
 * %%
 * Copyright (C) 2010 - 2021 Allen D. Ball
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ##########################################################################
 */
import ball.annotation.ServiceProviderFor;
import ball.annotation.processing.AnnotatedProcessor;
import ball.annotation.processing.For;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static java.util.stream.Collectors.toSet;
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
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
@ServiceProviderFor({ Processor.class })
@For({ LetterPremium.class, WordPremium.class })
@NoArgsConstructor @ToString
public class PremiumProcessor extends AnnotatedProcessor {
    @Override
    public void process(RoundEnvironment roundEnv,
                        TypeElement annotation, Element element) {
        super.process(roundEnv, annotation, element);

        switch (element.getKind()) {
        case CLASS:
            var set =
                getSupportedAnnotationTypeList().stream()
                .filter(t -> element.getAnnotation(t) != null)
                .map(t -> asTypeElement(t))
                .collect(toSet());

            set.remove(annotation);

            if (! set.isEmpty()) {
                print(ERROR, element,
                      "%s annotated with @%s but is also annotated with @%s",
                      element.getKind(),
                      annotation.getSimpleName(),
                      set.iterator().next().getSimpleName());
            }
            break;

        default:
            break;
        }
    }
}
