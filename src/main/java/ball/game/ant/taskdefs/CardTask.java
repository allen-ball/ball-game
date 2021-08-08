package ball.game.ant.taskdefs;
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
import ball.game.card.Card;
import ball.game.card.poker.Evaluator;
import ball.util.ant.taskdefs.AnnotatedAntTask;
import ball.util.ant.taskdefs.AntTask;
import ball.util.ant.taskdefs.ClasspathDelegateAntTask;
import ball.util.ant.taskdefs.ConfigurableAntTask;
import ball.util.ant.taskdefs.NotNull;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.util.ClasspathUtils;

import static lombok.AccessLevel.PROTECTED;

/**
 * Abstract {@link Card} {@link.uri http://ant.apache.org/ Ant}
 * {@link Task} base class.
 *
 * {@ant.task}
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
@NoArgsConstructor(access = PROTECTED)
public abstract class CardTask extends Task
                               implements AnnotatedAntTask,
                                          ClasspathDelegateAntTask,
                                          ConfigurableAntTask  {
    @Getter @Setter @Accessors(chain = true, fluent = true)
    private ClasspathUtils.Delegate delegate = null;

    @Override
    public void init() throws BuildException {
        super.init();
        ClasspathDelegateAntTask.super.init();
        ConfigurableAntTask.super.init();
    }

    @Override
    public void execute() throws BuildException {
        super.execute();
        AnnotatedAntTask.super.execute();
    }

    /**
     * {@link.uri http://ant.apache.org/ Ant}
     * {@link org.apache.tools.ant.Task} to evaluate a poker hand.
     *
     * {@ant.task}
     */
    @AntTask("poker-hand-evaluate")
    @NoArgsConstructor @ToString
    public static class PokerHandEvaluate extends CardTask {
        @NotNull @Getter
        private List<Card> cards = null;

        public void setCards(String string) {
            cards =
                Arrays.stream(string.split("[, ]+"))
                .map(t -> Card.parse(t))
                .collect(Collectors.toList());
        }

        @Override
        public void execute() throws BuildException {
            super.execute();

            try {
                var evaluator = new EvaluatorImpl(getCards());

                log(String.valueOf(evaluator.getRanking())
                    + ": " + String.valueOf(evaluator.getHand()));
            } catch (BuildException exception) {
                throw exception;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                throw new BuildException(throwable);
            }
        }

        private class EvaluatorImpl extends Evaluator {
            public EvaluatorImpl(Collection<Card> collection) {
                super(collection);
            }

            @Override
            public void accept(List<Card> list) {
                super.accept(list);

                log(String.valueOf(list) + " -> " + toString());
            }
        }
    }
}
