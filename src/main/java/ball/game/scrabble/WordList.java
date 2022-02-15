package ball.game.scrabble;
/*-
 * ##########################################################################
 * Game Applications and Utilities
 * %%
 * Copyright (C) 2010 - 2022 Allen D. Ball
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
import ball.annotation.CompileTimeCheck;
import java.text.Collator;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

/**
 * Abstract Word List base class.
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 */
public abstract class WordList extends TreeMap<CharSequence,Set<String>> {
    private static final long serialVersionUID = -2777411476474325653L;

    @CompileTimeCheck
    private static final Pattern BRACKETED = Pattern.compile("\\[[^]]+\\]");
    @CompileTimeCheck
    private static final Pattern ALTERNATIVES = Pattern.compile("-?[\\p{Upper}]+");

    /**
     * Sole constructor.
     *
     * @param   locale          The {@link Locale}.
     */
    protected WordList(Locale locale) {
        super(Collator.getInstance(locale));

        var bundle = ResourceBundle.getBundle(getClass().getName(), locale);

        for (var key : bundle.keySet()) {
            var value = bundle.getString(key);
            var root = key.toUpperCase();
            var line = String.join(" ", root, value).trim();

            add(root, line);

            var bracketed = BRACKETED.matcher(value);

            while (bracketed.find()) {
                var alternatives = ALTERNATIVES.matcher(bracketed.group());

                while (alternatives.find()) {
                    var word = alternatives.group();

                    if (word.startsWith("-")) {
                        word = root + word.replace("-", "");
                    }

                    add(word, line);
                }
            }
        }
    }

    private void add(String word, String source) {
        computeIfAbsent(word, k -> new LinkedHashSet<>()).add(source);
    }
}
