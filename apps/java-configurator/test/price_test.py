import jpype
import jpype.imports
from jpype.types import *

# === 1. Pornește JVM-ul și încarcă clasa Java ===
def start_jvm():
    if not jpype.isJVMStarted():
        jpype.startJVM(classpath=["../out/artifacts/ShowerConfiguratoir_jar"])

def stop_jvm():
    if jpype.isJVMStarted():
        jpype.shutdownJVM()


# === 2. Definim exemple de cabine pentru testare ===
def run_tests():
    from PriceCalculator import PriceCalculator  # Importă clasa Java

    tests = [
        {
            "name": "Cabină dreptunghiulară simplă",
            "input": {
                "latime": 120,
                "inaltime": 200,
                "material": "sticla",
                "grosime": 8,
                "profil": "aluminiu",
                "finisaj": "crom"
            },
            "expected": 1250.00
        },
        {
            "name": "Cabină tip colț cu sticlă fumurie",
            "input": {
                "latime": 100,
                "inaltime": 190,
                "material": "sticla_fumurie",
                "grosime": 10,
                "profil": "inox",
                "finisaj": "negru_mat"
            },
            "expected": 1575.50
        },
    ]

    calculator = PriceCalculator()  # instanțiem clasa Java

    for test in tests:
        inp = test["input"]

        # apel către metoda Java (înlocuiește cu semnătura reală din clasa ta)
        result = calculator.calculatePrice(
            JDouble(inp["latime"]),
            JDouble(inp["inaltime"]),
            inp["material"],
            JInt(inp["grosime"]),
            inp["profil"],
            inp["finisaj"]
        )

        print(f"\n=== Test: {test['name']} ===")
        print(f"Expected: {test['expected']}, Got: {result}")

        if abs(result - test["expected"]) < 0.01:
            print("✅ Passed")
        else:
            print("❌ Failed")


# === 3. Rulează testele ===
if __name__ == "__main__":
    start_jvm()
    try:
        run_tests()
    finally:
        stop_jvm()
