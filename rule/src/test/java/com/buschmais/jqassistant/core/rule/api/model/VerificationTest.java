package com.buschmais.jqassistant.core.rule.api.model;

import com.buschmais.jqassistant.core.rule.api.reader.AggregationVerification;
import com.buschmais.jqassistant.core.rule.api.reader.RowCountVerification;
import com.buschmais.jqassistant.core.rule.api.reader.RuleConfiguration;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.nullValue;

class VerificationTest {

    @Test
    void adoc() throws RuleException {
        RuleSet ruleSet = RuleSetTestHelper.readRuleSet("/resultVerification.adoc", RuleConfiguration.DEFAULT);
        verifyRuleSet(ruleSet);
    }

    @Test
    void xml() throws RuleException {
        RuleSet ruleSet = RuleSetTestHelper.readRuleSet("/resultVerification.xml", RuleConfiguration.DEFAULT);
        verifyRuleSet(ruleSet);
    }

    private void verifyRuleSet(RuleSet ruleSet) throws RuleException {
        verifyDefault(ruleSet);
        verifyCustomizedDefault(ruleSet);
        verifyAggregation(ruleSet);
        verifyRowCount(ruleSet);
    }

    private void verifyDefault(RuleSet ruleSet) throws RuleException {
        Concept concept = ruleSet.getConceptBucket().getById("test:DefaultVerification");
        Verification verification = concept.getVerification();
        assertThat(verification, nullValue());
    }

    private void verifyCustomizedDefault(RuleSet ruleSet) throws RuleException {
        Concept concept = ruleSet.getConceptBucket().getById("test:CustomizedDefaultVerification");
        Verification verification = concept.getVerification();
        assertThat(verification, instanceOf(RowCountVerification.class));
        RowCountVerification rowCountVerification = (RowCountVerification) verification;
        assertThat(rowCountVerification.getMin(), equalTo(1));
        assertThat(rowCountVerification.getMax(), equalTo(2));
    }

    private void verifyAggregation(RuleSet ruleSet) throws RuleException {
        Concept concept = ruleSet.getConceptBucket().getById("test:AggregationVerification");
        Verification verification = concept.getVerification();
        assertThat(verification, instanceOf(AggregationVerification.class));
        AggregationVerification aggregationVerification = (AggregationVerification) verification;
        assertThat(aggregationVerification.getMin(), equalTo(1));
        assertThat(aggregationVerification.getMax(), equalTo(2));
    }

    private void verifyRowCount(RuleSet ruleSet) throws RuleException {
        Concept concept = ruleSet.getConceptBucket().getById("test:RowCountVerification");
        Verification verification = concept.getVerification();
        assertThat(verification, instanceOf(RowCountVerification.class));
        RowCountVerification rowCountVerification = (RowCountVerification) verification;
        assertThat(rowCountVerification.getMin(), equalTo(1));
        assertThat(rowCountVerification.getMax(), equalTo(2));
    }
}
