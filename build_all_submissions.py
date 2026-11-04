import os
import subprocess
import shutil
import docx
from docx.shared import Inches, Pt, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT
from docx.oxml import parse_xml
from docx.oxml.ns import nsdecls

import generator_core as gc
from practicals_data import PRACTICALS
from generator_core import STUDENT_NAME, STUDENT_ID, REPO_URL, BASE_DIR, SUBMISSIONS_DIR, SCREENSHOTS_DIR

def run_problem_and_capture(prob_config):
    folder_rel = prob_config["folder"]
    folder_abs = os.path.join(BASE_DIR, folder_rel)
    compile_cmd = prob_config["compile"]
    run_cmd = prob_config["run"]
    inp = prob_config["input"]

    # Compile
    comp_res = subprocess.run(
        compile_cmd,
        shell=True,
        cwd=folder_abs,
        capture_output=True,
        text=True
    )
    if comp_res.returncode != 0:
        print(f"Compilation error in {folder_rel}: {comp_res.stderr}")

    # Run
    run_res = subprocess.run(
        run_cmd,
        shell=True,
        cwd=folder_abs,
        input=inp,
        capture_output=True,
        text=True
    )

    raw_output = run_res.stdout
    lines = [line.rstrip() for line in raw_output.splitlines() if line.strip()]

    # If interactive input was given, format lines nicely to show user input alongside prompts
    formatted_lines = []
    if inp:
        inp_tokens = [tok.strip() for tok in inp.split("\n") if tok.strip()]
        token_idx = 0
        for l in lines:
            # Check if this line ends with a prompt where user entered something
            if any(p in l for p in [":", "?", "Choice:", "stop):"]) and token_idx < len(inp_tokens):
                # If prompt doesn't already have the token appended
                if not any(t in l for t in inp_tokens):
                    l = f"{l} {inp_tokens[token_idx]}"
                    token_idx += 1
            formatted_lines.append(l)
    else:
        formatted_lines = lines

    # Read code files
    code_blocks = []
    for fname in prob_config["files"]:
        fpath = os.path.join(folder_abs, fname)
        if os.path.exists(fpath):
            with open(fpath, "r", encoding="utf-8") as f:
                code_blocks.append((fname, f.read()))

    return formatted_lines, code_blocks

def build_single_practical_doc(p):
    p_no = p["no"]
    p_title = p["title"]
    doc = docx.Document()
    gc.setup_page_header_footer(doc, p_no, p_title)
    gc.add_document_header(doc, p_no, p_title)

    # Problem 1
    prob1 = p["prob1"]
    folder_path1 = f"D:\\3rd sem\\JAVA\\{prob1['folder']}"
    out1_lines, code1 = run_problem_and_capture(prob1)
    ss1_path = os.path.join(SCREENSHOTS_DIR, f"Practical{p_no:02d}_Problem1.png")
    gc.render_terminal_screenshot(
        ss1_path,
        folder_path1,
        [(prob1["compile"], ""), (prob1["run"], "")],
        out1_lines
    )
    gc.add_problem_section(doc, 1, prob1["title"], prob1["stmt"], code1, ss1_path)

    # Problem 2
    prob2 = p["prob2"]
    folder_path2 = f"D:\\3rd sem\\JAVA\\{prob2['folder']}"
    out2_lines, code2 = run_problem_and_capture(prob2)
    ss2_path = os.path.join(SCREENSHOTS_DIR, f"Practical{p_no:02d}_Problem2.png")
    gc.render_terminal_screenshot(
        ss2_path,
        folder_path2,
        [(prob2["compile"], ""), (prob2["run"], "")],
        out2_lines
    )
    gc.add_problem_section(doc, 2, prob2["title"], prob2["stmt"], code2, ss2_path)

    doc_filename = f"{STUDENT_ID}_{STUDENT_NAME}_Practical{p_no:02d}.docx"
    doc_path = os.path.join(SUBMISSIONS_DIR, doc_filename)
    doc.save(doc_path)
    print(f"Generated: {doc_filename}")
    return doc_path, code1, code2, out1_lines, out2_lines, ss1_path, ss2_path

def build_master_doc(all_results):
    doc = docx.Document()
    gc.setup_page_header_footer(doc, 0, "Complete Practical File (Practicals 01 - 14)")

    # Cover Page
    tbl_cover = doc.add_table(rows=1, cols=1)
    tbl_cover.autofit = False
    tbl_cover.columns[0].width = Inches(7.0)
    c_cover = tbl_cover.cell(0, 0)
    gc.set_cell_background(c_cover, "0A2540")
    gc.set_cell_border_box(c_cover, "0A2540")

    p = c_cover.paragraphs[0]
    p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    p.paragraph_format.space_before = Pt(36)
    p.paragraph_format.space_after = Pt(4)

    r = p.add_run("FACULTY OF ENGINEERING AND TECHNOLOGY\n")
    r.font.name = "Calibri"
    r.font.size = Pt(16)
    r.font.bold = True
    r.font.color.rgb = RGBColor(255, 255, 255)

    r = p.add_run("DEPARTMENT OF COMPUTER SCIENCE & ENGINEERING\n\n")
    r.font.name = "Calibri"
    r.font.size = Pt(13)
    r.font.color.rgb = RGBColor(200, 220, 240)

    r = p.add_run("OBJECT ORIENTED PROGRAMMING (CEUE203)\n")
    r.font.name = "Calibri"
    r.font.size = Pt(18)
    r.font.bold = True
    r.font.color.rgb = RGBColor(255, 215, 0)

    r = p.add_run("LABORATORY PRACTICAL RECORD JOURNAL\n\n\n")
    r.font.name = "Calibri"
    r.font.size = Pt(14)
    r.font.color.rgb = RGBColor(255, 255, 255)

    r = p.add_run("Submitted By:\n")
    r.font.name = "Calibri"
    r.font.size = Pt(12)
    r.font.italic = True
    r.font.color.rgb = RGBColor(200, 220, 240)

    r = p.add_run(f"Name : {STUDENT_NAME}\nEnrolment No : {STUDENT_ID}\nSemester : 3rd Semester\nBranch : Computer Engineering\n\n")
    r.font.name = "Calibri"
    r.font.size = Pt(13)
    r.font.bold = True
    r.font.color.rgb = RGBColor(255, 255, 255)

    r = p.add_run(f"GitHub Repository:\n{REPO_URL}\n\n")
    r.font.name = "Calibri"
    r.font.size = Pt(10.5)
    r.font.underline = True
    r.font.color.rgb = RGBColor(102, 178, 255)
    p.paragraph_format.space_after = Pt(36)

    doc.add_page_break()

    # Index / Table of Contents
    h_idx = doc.add_heading("PRACTICAL INDEX", level=1)
    h_idx.runs[0].font.name = "Calibri"
    h_idx.runs[0].font.color.rgb = RGBColor(0, 51, 102)

    tbl_idx = doc.add_table(rows=1, cols=4)
    tbl_idx.autofit = False
    tbl_idx.columns[0].width = Inches(0.8)
    tbl_idx.columns[1].width = Inches(3.6)
    tbl_idx.columns[2].width = Inches(1.8)
    tbl_idx.columns[3].width = Inches(0.8)

    hdr_cells = tbl_idx.rows[0].cells
    hdr_titles = ["Prac No", "Practical Title & Problem Focus", "Student ID", "Status"]
    for i, title in enumerate(hdr_titles):
        gc.set_cell_background(hdr_cells[i], "0066CC")
        p_c = hdr_cells[i].paragraphs[0]
        r_c = p_c.add_run(title)
        r_c.font.name = "Calibri"
        r_c.font.bold = True
        r_c.font.size = Pt(9.5)
        r_c.font.color.rgb = RGBColor(255, 255, 255)

    for p in PRACTICALS:
        row_cells = tbl_idx.add_row().cells
        row_cells[0].paragraphs[0].add_run(f"Prac {p['no']:02d}")
        row_cells[1].paragraphs[0].add_run(f"{p['title']}\n• {p['prob1']['title']}\n• {p['prob2']['title']}")
        row_cells[2].paragraphs[0].add_run(f"{STUDENT_ID} ({STUDENT_NAME})")
        row_cells[3].paragraphs[0].add_run("Completed")

        for c in row_cells:
            gc.set_cell_background(c, "F6F8FA" if p['no'] % 2 == 0 else "FFFFFF")
            gc.set_cell_border_box(c, "D0D7DE")
            p_text = c.paragraphs[0]
            for r in p_text.runs:
                r.font.name = "Calibri"
                r.font.size = Pt(9)

    doc.add_page_break()

    # Add each practical
    for res in all_results:
        p = res["practical"]
        p_no = p["no"]
        p_title = p["title"]

        gc.add_document_header(doc, p_no, p_title)
        gc.add_problem_section(doc, 1, p["prob1"]["title"], p["prob1"]["stmt"], res["code1"], res["ss1"])
        gc.add_problem_section(doc, 2, p["prob2"]["title"], p["prob2"]["stmt"], res["code2"], res["ss2"])
        doc.add_page_break()

    master_filename = f"{STUDENT_ID}_{STUDENT_NAME}_All_Practicals.docx"
    master_path = os.path.join(SUBMISSIONS_DIR, master_filename)
    doc.save(master_path)
    print(f"Generated Master Record: {master_filename}")
    return master_path

def main():
    print(f"Starting complete submission document generation for {STUDENT_NAME} ({STUDENT_ID})...")
    all_results = []
    for p in PRACTICALS:
        doc_path, code1, code2, out1, out2, ss1, ss2 = build_single_practical_doc(p)
        all_results.append({
            "practical": p,
            "code1": code1,
            "code2": code2,
            "out1": out1,
            "out2": out2,
            "ss1": ss1,
            "ss2": ss2
        })

    build_master_doc(all_results)
    print("\nAll 14 individual practical documents and master document generated successfully!")

if __name__ == "__main__":
    main()
