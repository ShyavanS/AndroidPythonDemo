import os
import json
import numpy as np

try:
    with open(os.path.join(os.environ["HOME"], "matrix.json"), "r") as stdin:
        MATRIX_DICT = json.load(stdin)

        ALPHABET = MATRIX_DICT['alphabet']
        ENCIPHERING_MATRIX = np.array(MATRIX_DICT['enciphering_matrix'])
except FileNotFoundError:
    with open(os.path.join(os.environ["HOME"], "matrix.json"), "w") as stdout:
        MATRIX_DICT = {
            "enciphering_matrix": [[3, 5], [6, 7]],
            "alphabet": {
                "a": 0,
                "b": 1,
                "c": 2,
                "d": 3,
                "e": 4,
                "f": 5,
                "g": 6,
                "h": 7,
                "i": 8,
                "j": 9,
                "k": 10,
                "l": 11,
                "m": 12,
                "n": 13,
                "o": 14,
                "p": 15,
                "q": 16,
                "r": 17,
                "s": 18,
                "t": 19,
                "u": 20,
                "v": 21,
                "w": 22,
                "x": 23,
                "y": 24,
                "z": 25
            }
        }
        ALPHABET = MATRIX_DICT['alphabet']
        ENCIPHERING_MATRIX = np.array(MATRIX_DICT['enciphering_matrix'])

        json.dump(MATRIX_DICT, stdout, indent=4)


class HillCipherUndefined(Exception):
    pass


def hill_cipher(stdin):
    length = len(stdin)

    for char in stdin:
        if char.isdigit() or char == ' ':
            invalid = True
        else:
            invalid = False

    if (length % 2) or (invalid):
        raise HillCipherUndefined(
            "The message to be encrypted must be of even length and contain no digits and spaces in order to be enciphered by a 2 by 2 enciphering matrix.")

    text_vectors = np.array([[[ALPHABET[f"{stdin[i]}".lower()]], [
                            ALPHABET[f"{stdin[i + 1]}".lower()]]] for i in range(0, length, 2)])

    encrypted_vectors = np.array(
        [ENCIPHERING_MATRIX.dot(v) % 26 for v in text_vectors])

    encrypted_list = encrypted_vectors.flatten().tolist()
    encrypted_stdin = ''.join([list(ALPHABET.keys())[list(
        ALPHABET.values()).index(i)] for i in encrypted_list])

    return encrypted_stdin


if __name__ == "__main__":
    print(hill_cipher("test"))
