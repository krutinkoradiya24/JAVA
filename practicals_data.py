import os
import subprocess
import shutil
import docx
from docx.shared import Inches, Pt, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.oxml import parse_xml
from docx.oxml.ns import nsdecls
from PIL import Image, ImageDraw, ImageFont
import generator_core as gc

BASE_DIR = os.path.abspath(os.path.dirname(__file__))
STUDENT_NAME = "Krutin"
STUDENT_ID = "25CS034"
REPO_URL = "https://github.com/krutinkoradiya24/JAVA.git"

PRACTICALS = [
    {
        "no": 1,
        "title": "Enums, Records, and Switch Expressions",
        "prob1": {
            "title": "Vending Machine",
            "folder": "lab1\\pra1",
            "files": ["VendingMachine.java"],
            "compile": "javac VendingMachine.java",
            "run": "java VendingMachine",
            "input": "TEN\nFIVE\n",
            "stmt": """(a) Define an enum Coin with constants ONE, TWO, FIVE, TEN.
(b) In main, set a snack price of 15 and a running total of 0; create a Scanner.
(c) Loop: read a coin name, use a switch expression to convert the Coin to its value (ONE->1, TWO->2, FIVE->5, TEN->10), add it to the total, and print the total so far.
(d) Stop the loop once the total reaches 15 or more.
(e) Print the change to return (total - 15).
Expected output: TEN then FIVE prints "Paid. Change: 0"; TEN then TEN prints "Paid. Change: 5"."""
        },
        "prob2": {
            "title": "Toll Booth Management",
            "folder": "lab1\\pra2",
            "files": ["TollBooth.java"],
            "compile": "javac TollBooth.java",
            "run": "java TollBooth",
            "input": "GJ01 car\nGJ02 car\nGJ03 bike\ndone\n",
            "stmt": """(a) Define a record Vehicle(String number, String type).
(b) In main, keep a running toll total and three counters (bike, car, truck).
(c) Loop until the user types "done" for the number: build a Vehicle, use a switch expression on its type for the toll (bike->20, car->50, truck->150), add to the total, and increment that type's counter.
(d) After the loop, print the total toll and the type with the highest count.
Expected output: two cars + one bike -> "Total toll: 120", "Most frequent: car"."""
        }
    },
    {
        "no": 2,
        "title": "Classes, Constructors, Encapsulation, and Static Members",
        "prob1": {
            "title": "Smart Thermostat",
            "folder": "lab2\\pra1",
            "files": ["Thermostat.java"],
            "compile": "javac Thermostat.java",
            "run": "java Thermostat",
            "input": None,
            "stmt": """(a) Create class Thermostat with private String location, private int temperature, private static final int MIN=16 and MAX=30, and private static int activeCount=0.
(b) Constructor(location, startTemp): store location; set temperature to startTemp if it is within MIN..MAX else 22; increment activeCount.
(c) Constructor(location): chain with this(location, 22).
(d) raise(): if temperature < MAX add 1, else print "Already at maximum (30)".
(e) lower(): if temperature > MIN subtract 1, else print "Already at minimum (16)".
(f) Add getTemperature() (no setter) and static getActiveCount().
(g) In main: create two thermostats, call raise() 10 times then lower() 20 times in loops, then print getActiveCount()."""
        },
        "prob2": {
            "title": "Cinema Show Booking",
            "folder": "lab2\\pra2",
            "files": ["CinemaShow.java"],
            "compile": "javac CinemaShow.java",
            "run": "java CinemaShow",
            "input": None,
            "stmt": """(a) Create class CinemaShow with private String title, private int seatsAvailable, private final int capacity, and private static int totalBooked=0.
(b) Constructor(title, capacity): set both and seatsAvailable = capacity. Constructor(title): chain with this(title, 100).
(c) book(int n): if n <= seatsAvailable, reduce seatsAvailable by n, add n to totalBooked, return true; else return false unchanged.
(d) cancel(int n): increase seatsAvailable by n but never above capacity.
(e) Add getSeatsAvailable() and static getTotalBooked().
(f) In main: run a sequence of book and cancel calls, printing the result and seats after each, then print getTotalBooked()."""
        }
    },
    {
        "no": 3,
        "title": "Overriding Object-Class Methods (equals, hashCode, toString)",
        "prob1": {
            "title": "Distinct Coordinates Filter",
            "folder": "lab3\\pra1",
            "files": ["Point.java", "Driver.java"],
            "compile": "javac Point.java Driver.java",
            "run": "java Driver",
            "input": None,
            "stmt": """(a) Create class Point with private int x, y and a constructor.
(b) Override toString() to return "(x, y)".
(c) Override equals(Object) to be true when both x and y match, and hashCode() to agree (e.g. Objects.hash(x, y)).
(d) In Driver.main: build a Point[] with some repeated coordinates.
(e) Count distinct points: for each point, use equals() to check whether it already appeared earlier; count those that did not.
(f) Print "Distinct: N".
Expected output: 5 points with 2 repeats -> "Distinct: 3"."""
        },
        "prob2": {
            "title": "Duplicate Playing Card Detector",
            "folder": "lab3\\pra2",
            "files": ["Card.java", "Driver.java"],
            "compile": "javac Card.java Driver.java",
            "run": "java Driver",
            "input": None,
            "stmt": """(a) Create class Card with private String rank, suit and a constructor.
(b) Override toString() to return rank + " of " + suit (e.g. "Queen of Hearts").
(c) Override equals()/hashCode() by rank + suit.
(d) In Driver.main: add cards to a Card[] one at a time; before adding each, use equals() to check it against the earlier cards.
(e) Print "Duplicate found: <card>" the first time a repeat appears."""
        }
    },
    {
        "no": 4,
        "title": "String Processing, Regular Expressions, and StringBuilder",
        "prob1": {
            "title": "Password Strength Evaluator",
            "folder": "lab4\\pra1",
            "files": ["PasswordChecker.java", "Driver.java"],
            "compile": "javac PasswordChecker.java Driver.java",
            "run": "java Driver",
            "input": None,
            "stmt": """(a) In PasswordChecker, check four rules - length >= 8, contains an uppercase letter, contains a digit, contains a special character - using regex or String methods.
(b) Write strength(String pw) that counts how many rules pass and returns "Weak" (0-1), "Medium" (2-3) or "Strong" (4).
(c) In Driver.main: test several passwords; for each, print which rules passed and the final label."""
        },
        "prob2": {
            "title": "Chat-Log Stream Filter",
            "folder": "lab4\\pra2",
            "files": ["ChatFilter.java", "Driver.java"],
            "compile": "javac ChatFilter.java Driver.java",
            "run": "java Driver",
            "input": "hello\n",
            "stmt": """(a) In Driver.main, define a String[] of log lines like "10:05 alice Hello there" (include one malformed line with fewer than 3 parts).
(b) Read a keyword from the user.
(c) For each line, split into time/user/message with split(" ", 3); skip lines that do not have 3 parts.
(d) If the message contains the keyword (case-insensitive), increment a counter and append "time user: message" to a StringBuilder.
(e) Print "Matches: N" and the built report."""
        }
    },
    {
        "no": 5,
        "title": "Inheritance, Abstract Classes, and Polymorphism",
        "prob1": {
            "title": "Polymorphic Shape Area Calculation",
            "folder": "lab5\\pra1",
            "files": ["Main.java"],
            "compile": "javac Main.java",
            "run": "java Main",
            "input": None,
            "stmt": """Define an abstract Shape with abstract area(); subclasses Circle, Rectangle, Triangle.
Put a mix of shapes in a Shape[] array and loop once to print each area and the running total, tracking the largest shape area - demonstrating one loop handling every shape type through polymorphism."""
        },
        "prob2": {
            "title": "Employee Payroll & Type Checking",
            "folder": "lab5\\pra2",
            "files": ["Main.java"],
            "compile": "javac Main.java",
            "run": "java Main",
            "input": None,
            "stmt": """Define an abstract Employee with abstract monthlySalary(); subclasses FullTime (fixed salary), PartTime (hours x rate) and Intern (stipend), each calling super constructor for shared name/id fields.
Loop through a mixed array to print each salary and compute total payroll; use the instanceof operator to identify and print a special note only for interns."""
        }
    },
    {
        "no": 6,
        "title": "Interfaces, Default Methods, Functional Interfaces, and Marker Interfaces",
        "prob1": {
            "title": "Remote Control Switchable Hierarchy",
            "folder": "lab6\\pra1",
            "files": ["RemoteControlDemo.java"],
            "compile": "javac RemoteControlDemo.java",
            "run": "java RemoteControlDemo",
            "input": None,
            "stmt": """Create an interface Switchable with on()/off() and a default toggle() method; classes Fan and Light implement it.
Loop over a Switchable[] toggling each device. Then add a @FunctionalInterface SwitchPolicy that decides, given a device and an hour, whether it may switch on, and supply it once as an anonymous class and once as a lambda expression."""
        },
        "prob2": {
            "title": "Broadcast Notification System with Marker Interface",
            "folder": "lab6\\pra2",
            "files": ["NotificationDemo.java"],
            "compile": "javac NotificationDemo.java",
            "run": "java NotificationDemo",
            "input": None,
            "stmt": """Create a functional interface Notifier with send(String); instantiate email and SMS senders using lambda expressions, hold them in an array, and broadcast one message through all.
Add an empty marker interface Urgent and implement it to dispatch messages twice for senders marked urgent."""
        }
    },
    {
        "no": 7,
        "title": "Custom Annotations and Runtime Reflection",
        "prob1": {
            "title": "Reflection-Based Form Validator",
            "folder": "lab7\\pra1",
            "files": ["Main.java"],
            "compile": "javac Main.java",
            "run": "java Main",
            "input": None,
            "stmt": """Define custom runtime annotations @NotBlank and @MaxLength(int) for object fields and annotate a SignupForm class.
Write a reflection validator that inspects getDeclaredFields(), reads field annotations at runtime, checks constraints, and returns a list of validation error messages."""
        },
        "prob2": {
            "title": "Custom Unit Test Runner (@Run)",
            "folder": "lab7\\pra2",
            "files": ["Main.java"],
            "compile": "javac Main.java",
            "run": "java Main",
            "input": None,
            "stmt": """Define a @Run annotation with RUNTIME retention and METHOD target. Mark several no-arg test methods of a class with @Run.
Use Java reflection to inspect declared methods, invoke only those annotated with @Run, and print execution results and total tests executed."""
        }
    },
    {
        "no": 8,
        "title": "Exception Handling and Custom Checked Exceptions",
        "prob1": {
            "title": "Guarded Robust Calculator",
            "folder": "lab8\\pra1",
            "files": ["GuardedCalculator.java"],
            "compile": "javac GuardedCalculator.java",
            "run": "java GuardedCalculator",
            "input": "10 / 0\nabc\n10 + 5\n",
            "stmt": """Create a guarded calculator that reads two numbers and an operator.
Throw a custom DivideByZeroException when denominator is 0, catch InputMismatchException for non-numeric input separately, retry in a loop until valid, and use a finally block to log each calculation attempt."""
        },
        "prob2": {
            "title": "Warehouse Stock Issue System",
            "folder": "lab8\\pra2",
            "files": ["StockIssue.java"],
            "compile": "javac StockIssue.java",
            "run": "java StockIssue",
            "input": None,
            "stmt": """Model a Warehouse inventory where issue(item, qty) throws a custom checked OutOfStockException carrying the shortfall amount when inventory is insufficient, and InvalidQuantityException for qty <= 0.
Process a list of order requests, catching and reporting each specific failure gracefully without terminating the program."""
        }
    },
    {
        "no": 9,
        "title": "Multithreading, Race Conditions, and Synchronization",
        "prob1": {
            "title": "Shared Counter Race Condition & Fix",
            "folder": "lab9\\pra1",
            "files": ["CounterRace.java"],
            "compile": "javac CounterRace.java",
            "run": "java CounterRace",
            "input": None,
            "stmt": """Demonstrate thread race conditions: create 10 concurrent threads each incrementing a shared counter 10,000 times.
Run once without synchronization to observe lost updates (actual < expected), and then demonstrate the fix using a synchronized method, confirming the exact expected count (100,000) every run."""
        },
        "prob2": {
            "title": "Seat Booking Race & Thread Safety",
            "folder": "lab9\\pra2",
            "files": ["SeatBookingRace.java"],
            "compile": "javac SeatBookingRace.java",
            "run": "java SeatBookingRace",
            "input": None,
            "stmt": """Model a shared cinema booking system with 5 remaining seats and 10 concurrent customer threads trying to book seats.
Demonstrate that without synchronization overselling occurs (negative remaining seats). Fix the race condition with a synchronized book() method to ensure exactly 5 seats are booked and the rest rejected cleanly."""
        }
    },
    {
        "no": 10,
        "title": "ExecutorService Thread Pool & Inter-Thread Coordination",
        "prob1": {
            "title": "Fixed Thread-Pool Task Runner",
            "folder": "lab10\\pra1",
            "files": ["ThreadPoolRunner.java"],
            "compile": "javac ThreadPoolRunner.java",
            "run": "java ThreadPoolRunner",
            "input": None,
            "stmt": """Create a fixed thread pool of 3 worker threads using Executors.newFixedThreadPool(3).
Submit 10 Runnable tasks that print their task ID and executing thread name, sleep briefly to simulate work, shut down gracefully, and await completion using awaitTermination()."""
        },
        "prob2": {
            "title": "Producer-Consumer with wait() and notify()",
            "folder": "lab10\\pra2",
            "files": ["ProducerConsumer.java"],
            "compile": "javac ProducerConsumer.java",
            "run": "java ProducerConsumer",
            "input": None,
            "stmt": """Implement the classic Producer-Consumer pattern using a synchronized SharedBuffer.
Coordinate producer and consumer threads using intrinsic locks with wait() and notify() methods, guaranteeing sequential in-order item production and consumption with zero data loss."""
        }
    },
    {
        "no": 11,
        "title": "Object Serialization and Java NIO File Handling",
        "prob1": {
            "title": "Object Serialization with Transient Fields",
            "folder": "lab11\\pra1",
            "files": ["Saveload.java"],
            "compile": "javac Saveload.java",
            "run": "java Saveload",
            "input": None,
            "stmt": """Define a Serializable Student class with an id, name, and transient password field.
Serialize an array of student objects to a binary file using ObjectOutputStream inside try-with-resources. Deserialize the array back using ObjectInputStream, demonstrating that serialized fields survive while transient fields remain null."""
        },
        "prob2": {
            "title": "NIO Directory Stream & Log Analyzer",
            "folder": "lab11\\pra2",
            "files": ["Loganalyzer.java"],
            "compile": "javac Loganalyzer.java",
            "run": "java Loganalyzer",
            "input": None,
            "stmt": """Create multiple log text files using modern Java NIO (Files, Path).
Iterate through the directory using Files.newDirectoryStream(), read line-by-line with BufferedReader, count total lines and keyword matches ("ERROR"), and inspect file sizes using Files.readAttributes()."""
        }
    },
    {
        "no": 12,
        "title": "Java Collections Framework (HashMap, Set, and Custom Sorting)",
        "prob1": {
            "title": "Word Frequency Analyzer with Comparator",
            "folder": "lab12\\pra1",
            "files": ["WordFrequency.java"],
            "compile": "javac WordFrequency.java",
            "run": "java WordFrequency",
            "input": None,
            "stmt": """Tokenize a paragraph into lowercase words, count the occurrences of each word using a HashMap<String, Integer>, and sort the entry set in descending order of frequency using a custom Comparator / lambda."""
        },
        "prob2": {
            "title": "Unique Visitors Tracker (HashSet & LinkedHashSet)",
            "folder": "lab12\\pra2",
            "files": ["UniqueVisitors.java"],
            "compile": "javac UniqueVisitors.java",
            "run": "java UniqueVisitors",
            "input": None,
            "stmt": """From a stream of user login names containing duplicates, use a HashSet to count unique visitors and a LinkedHashSet to preserve first-seen insertion order.
Print the total distinct visitor count and display the first three distinct visitors in order."""
        }
    },
    {
        "no": 13,
        "title": "Java Generics (Generic Classes, Methods, and Bounded Types)",
        "prob1": {
            "title": "Generic Containers (Box & Pair)",
            "folder": "lab13\\pra1",
            "files": ["Box.java", "Pair.java", "GenericDemo.java"],
            "compile": "javac Box.java Pair.java GenericDemo.java",
            "run": "java GenericDemo",
            "input": None,
            "stmt": """Implement generic container classes Box<T> and Pair<A, B> with getter and setter methods.
Demonstrate compile-time type safety with different types (Integer, String) without explicit casting, and write a generic method <T> T firstElement(T[] array) that returns the first element of any array."""
        },
        "prob2": {
            "title": "Bounded Type Generic Method",
            "folder": "lab13\\pra2",
            "files": ["BoundedStats.java"],
            "compile": "javac BoundedStats.java",
            "run": "java BoundedStats",
            "input": None,
            "stmt": """Write a bounded type generic method <N extends Number> double average(N[] values) that computes the arithmetic mean for any numeric array (Integer[], Double[], Long[]).
Demonstrate how the upper bound ensures type safety and allows calling Number methods like doubleValue()."""
        }
    },
    {
        "no": 14,
        "title": "Layered Application Architecture, Testing, and State Persistence",
        "prob1": {
            "title": "Modular Layered Student Management Service",
            "folder": "lab14\\pra1",
            "files": ["Student.java", "StudentService.java", "Main.java", "StudentServiceTest.java"],
            "compile": "javac Student.java StudentService.java Main.java",
            "run": "java Main",
            "input": "1\n101\nRahul\nCSE\n2\n3\n",
            "stmt": """Build a layered application featuring a model entity (Student), an in-memory service layer (StudentService backed by ArrayList), and an interactive console interface (Main) for managing records, accompanied by JUnit unit tests validating business rules."""
        },
        "prob2": {
            "title": "Application State Serialization Persistence",
            "folder": "lab14\\pra2",
            "files": ["App.java"],
            "compile": "javac App.java",
            "run": "java App",
            "input": None,
            "stmt": """Design a standalone persistent application that encapsulates student records into serializable objects, writes state to disk on exit, and reloads data upon startup verifying state persistence."""
        }
    }
]

print(f"Loaded {len(PRACTICALS)} practical configurations.")
