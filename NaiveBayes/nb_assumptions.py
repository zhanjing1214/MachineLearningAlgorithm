#This scrip implements naive bayes model for a binary dataset which includes 4 features and a label
#It will train the classifier on the datapoints in train set
#and print out the probability that the datapoint is 1 from the model in the test set

#author: Jing Zhan

import sys

def train(f1):
    f = open(f1, "r")
    line = f.readline().rstrip()
    att_num = len(line.split(" ")) - 1
    count_1 = 0
    likely_0 = [0] * att_num
    likely_1 = [0] * att_num
    count_all = 0
    while line != "":
        record = line.split(" ")
        count_all += 1
        if record[att_num] == "1":
            count_1 += 1
            for i in range(att_num):
                if record[i] == "1":
                    likely_1[i] += 1
                else:
                    likely_0[i] += 1
        line = f.readline().rstrip()
    prob_0_v1 = [i/(count_1+0.0) for i in likely_0]
    prob_1_v1 = [i/(count_1+0.0) for i in likely_1]
    prior_1 = (count_1 + 0.0) / count_all
    return prior_1, prob_0_v1, prob_1_v1


def classify_nb(f1, f2):
    prior_1, prob_0_v1, prob_1_v1 = train(f1)
    f = open(f2, "r")
    line = f.readline().rstrip()
    att_num = len(line.split(" ")) - 1
    while line != "":
        record = line.split(" ")
        prob = prior_1
        for i in range(att_num):
            if record[i] == "0":
                prob *= prob_0_v1[i]
            else:
                prob *= prob_1_v1[i]
        print(prob * (2**att_num))
        line = f.readline().rstrip()


#python nb_assumptions.py 4-features.train 4-features.test
def main():
    classify_nb(sys.argv[1], sys.argv[2])


main()
