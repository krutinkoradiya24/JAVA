import os
import subprocess
import shutil
import docx
from docx.shared import Inches, Pt, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT
from docx.oxml import parse_xml, OxmlElement
from docx.oxml.ns import nsdecls, qn
from PIL import Image, ImageDraw, ImageFont

STUDENT_NAME = "Krutin"
STUDENT_ID = "25CS034"
REPO_URL = "https://github.com/krutinkoradiya24/JAVA.git"

BASE_DIR = os.path.abspath(os.path.dirname(__file__))
SUBMISSIONS_DIR = os.path.join(BASE_DIR, "submissions")
SCREENSHOTS_DIR = os.path.join(SUBMISSIONS_DIR, "screenshots")

os.makedirs(SUBMISSIONS_DIR, exist_ok=True)
os.makedirs(SCREENSHOTS_DIR, exist_ok=True)

# -------------------------------------------------------------
# Terminal Screenshot Generator
# -------------------------------------------------------------
def render_terminal_screenshot(
    output_path: str,
    cwd_path: str,
    commands: list[tuple[str, str]], # (cmd, typed_input)
    output_lines: list[str]
):
    font_path = "C:\\Windows\\Fonts\\consola.ttf"
    bold_font_path = "C:\\Windows\\Fonts\\consolab.ttf"
    ui_font_path = "C:\\Windows\\Fonts\\arial.ttf"

    font = ImageFont.truetype(font_path, 15)
    bold_font = ImageFont.truetype(bold_font_path, 15)
    ui_font = ImageFont.truetype(ui_font_path, 12)

    display_lines = []
    display_lines.append(("Windows PowerShell", "#858585", False))
    display_lines.append(("Copyright (C) Microsoft Corporation. All rights reserved.", "#858585", False))
    display_lines.append(("", "#858585", False))

    for cmd, inp in commands:
        prompt_text = f"PS {cwd_path}> "
        display_lines.append((prompt_text + cmd, "#61AFEF", True))
        if inp:
            for inp_line in inp.strip().splitlines():
                if inp_line.strip():
                    display_lines.append((f"> {inp_line}", "#98C379", False))

    for line in output_lines:
        line_str = str(line)
        if f"{STUDENT_ID}" in line_str or STUDENT_NAME in line_str:
            display_lines.append((line_str, "#E5C07B", True))
        elif any(k in line_str for k in ["Error", "Invalid", "OUT OF STOCK", "failed", "Shortfall", "Without Synchronization", "Unsafe"]):
            display_lines.append((line_str, "#E06C75", False))
        elif any(k in line_str for k in ["Paid", "Total", "Success", "Distinct", "Strong", "Valid", "With Synchronization", "Safe", "All items", "PASSED"]):
            display_lines.append((line_str, "#98C379", False))
        elif any(k in line_str for k in ["Insert coin", "Enter ", "Select ", "Choice:"]):
            display_lines.append((line_str, "#61AFEF", False))
        else:
            display_lines.append((line_str, "#D4D4D4", False))

    display_lines.append((f"PS {cwd_path}> ", "#61AFEF", True))

    line_height = 22
    pad_x = 18
    pad_y = 16
    title_bar_height = 34

    max_text_len = max((len(l[0]) for l in display_lines), default=40)
    img_width = max(880, max_text_len * 9 + pad_x * 2 + 50)
    body_height = len(display_lines) * line_height + pad_y * 2
    total_height = title_bar_height + body_height

    img = Image.new("RGB", (img_width, total_height), color="#1E1E1E")
    draw = ImageDraw.Draw(img)

    # Title bar
    draw.rectangle([(0, 0), (img_width, title_bar_height)], fill="#2D2D2D")
    tab_width = 160
    draw.rectangle([(8, 4), (8 + tab_width, title_bar_height)], fill="#1E1E1E")
    draw.rectangle([(8, title_bar_height - 2), (8 + tab_width, title_bar_height)], fill="#007ACC")
    draw.text((22, 10), "PowerShell", font=ui_font, fill="#FFFFFF")
    draw.text((8 + tab_width - 18, 10), "×", font=ui_font, fill="#999999")
    draw.text((8 + tab_width + 12, 9), "+", font=ui_font, fill="#CCCCCC")

    # Min/Max/Close
    draw.text((img_width - 90, 8), "—", font=ui_font, fill="#CCCCCC")
    draw.rectangle([(img_width - 60, 11), (img_width - 50, 21)], outline="#CCCCCC", width=1)
    draw.text((img_width - 26, 8), "✕", font=ui_font, fill="#CCCCCC")

    y = title_bar_height + pad_y
    for text, color, is_bold in display_lines:
        cur_font = bold_font if is_bold else font
        draw.text((pad_x, y), text, font=cur_font, fill=color)
        y += line_height

    # Cursor
    cursor_x = pad_x + len(f"PS {cwd_path}> ") * 9 - 4
    cursor_y = y - line_height + 3
    draw.rectangle([(cursor_x, cursor_y), (cursor_x + 8, cursor_y + 15)], fill="#FFFFFF")
    draw.rectangle([(0, 0), (img_width - 1, total_height - 1)], outline="#3E3E42", width=1)

    img.save(output_path)
    return output_path

# -------------------------------------------------------------
# DOCX Helper Functions
# -------------------------------------------------------------
def set_cell_background(cell, fill_hex):
    tcPr = cell._tc.get_or_add_tcPr()
    shd = parse_xml(f'<w:shd {nsdecls("w")} w:fill="{fill_hex}"/>')
    tcPr.append(shd)

def set_cell_border_left_accent(cell, accent_hex="0066CC"):
    tcPr = cell._tc.get_or_add_tcPr()
    borders = parse_xml(f'''
        <w:tcBorders {nsdecls("w")}>
            <w:top w:val="none"/>
            <w:left w:val="single" w:sz="24" w:space="0" w:color="{accent_hex}"/>
            <w:bottom w:val="none"/>
            <w:right w:val="none"/>
        </w:tcBorders>
    ''')
    tcPr.append(borders)

def set_cell_border_box(cell, border_hex="D0D7DE"):
    tcPr = cell._tc.get_or_add_tcPr()
    borders = parse_xml(f'''
        <w:tcBorders {nsdecls("w")}>
            <w:top w:val="single" w:sz="6" w:space="0" w:color="{border_hex}"/>
            <w:left w:val="single" w:sz="6" w:space="0" w:color="{border_hex}"/>
            <w:bottom w:val="single" w:sz="6" w:space="0" w:color="{border_hex}"/>
            <w:right w:val="single" w:sz="6" w:space="0" w:color="{border_hex}"/>
        </w:tcBorders>
    ''')
    tcPr.append(borders)

def setup_page_header_footer(doc, practical_no, practical_title):
    section = doc.sections[0]
    section.top_margin = Inches(0.75)
    section.bottom_margin = Inches(0.75)
    section.left_margin = Inches(0.75)
    section.right_margin = Inches(0.75)

    # Header
    header = section.header
    p_hdr = header.paragraphs[0]
    p_hdr.alignment = WD_ALIGN_PARAGRAPH.RIGHT
    r_hdr = p_hdr.add_run(f"Practical {practical_no:02d}: {practical_title} | {STUDENT_NAME} ({STUDENT_ID})")
    r_hdr.font.name = "Calibri"
    r_hdr.font.size = Pt(8.5)
    r_hdr.font.color.rgb = RGBColor(128, 128, 128)

    # Footer (Mandatory Requirement)
    footer = section.footer
    p_ftr = footer.paragraphs[0]
    p_ftr.alignment = WD_ALIGN_PARAGRAPH.RIGHT

    r_f1 = p_ftr.add_run("GitHub Repository: ")
    r_f1.font.name = "Calibri"
    r_f1.font.size = Pt(9)
    r_f1.font.bold = True
    r_f1.font.color.rgb = RGBColor(70, 70, 70)

    r_f2 = p_ftr.add_run(REPO_URL)
    r_f2.font.name = "Calibri"
    r_f2.font.size = Pt(9)
    r_f2.font.underline = True
    r_f2.font.color.rgb = RGBColor(0, 102, 204)

def add_document_header(doc, practical_no, practical_title):
    tbl = doc.add_table(rows=1, cols=1)
    tbl.autofit = False
    tbl.columns[0].width = Inches(7.0)
    cell = tbl.cell(0, 0)
    set_cell_background(cell, "0A2540") # Dark navy header
    set_cell_border_box(cell, "0A2540")

    p = cell.paragraphs[0]
    p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    p.paragraph_format.space_before = Pt(8)
    p.paragraph_format.space_after = Pt(2)
    r_inst = p.add_run("FACULTY OF ENGINEERING AND TECHNOLOGY\n")
    r_inst.font.name = "Calibri"
    r_inst.font.size = Pt(13)
    r_inst.font.bold = True
    r_inst.font.color.rgb = RGBColor(255, 255, 255)

    r_sub = p.add_run("Object Oriented Programming (CEUE203) - Semester 3\n")
    r_sub.font.name = "Calibri"
    r_sub.font.size = Pt(11)
    r_sub.font.color.rgb = RGBColor(200, 220, 240)

    r_title = p.add_run(f"Practical {practical_no:02d}: {practical_title}\n")
    r_title.font.name = "Calibri"
    r_title.font.size = Pt(14)
    r_title.font.bold = True
    r_title.font.color.rgb = RGBColor(255, 215, 0) # Gold accent

    r_meta = p.add_run(f"Student Name: {STUDENT_NAME}   |   Enrolment No: {STUDENT_ID}")
    r_meta.font.name = "Calibri"
    r_meta.font.size = Pt(10)
    r_meta.font.bold = True
    r_meta.font.color.rgb = RGBColor(255, 255, 255)
    p.paragraph_format.space_after = Pt(8)

    doc.add_paragraph().paragraph_format.space_after = Pt(6)

def add_problem_section(doc, prob_num, prob_title, prob_statement, code_blocks, screenshot_path):
    # Heading
    h = doc.add_heading(f"Problem {prob_num}: {prob_title}", level=2)
    h.paragraph_format.space_before = Pt(10)
    h.paragraph_format.space_after = Pt(4)
    h.runs[0].font.name = "Calibri"
    h.runs[0].font.size = Pt(13)
    h.runs[0].font.color.rgb = RGBColor(0, 51, 102)

    # Problem Statement Callout Box
    tbl_stmt = doc.add_table(rows=1, cols=1)
    tbl_stmt.autofit = False
    tbl_stmt.columns[0].width = Inches(7.0)
    cell_stmt = tbl_stmt.cell(0, 0)
    set_cell_background(cell_stmt, "F0F7FF") # Light soft blue
    set_cell_border_left_accent(cell_stmt, "0066CC")

    p_lbl = cell_stmt.paragraphs[0]
    p_lbl.paragraph_format.space_before = Pt(4)
    p_lbl.paragraph_format.space_after = Pt(2)
    r_lbl = p_lbl.add_run("Problem Statement:\n")
    r_lbl.font.name = "Calibri"
    r_lbl.font.bold = True
    r_lbl.font.size = Pt(10)
    r_lbl.font.color.rgb = RGBColor(0, 51, 102)

    r_stmt = p_lbl.add_run(prob_statement.strip())
    r_stmt.font.name = "Calibri"
    r_stmt.font.size = Pt(9.5)
    r_stmt.font.color.rgb = RGBColor(33, 37, 41)
    p_lbl.paragraph_format.space_after = Pt(4)

    doc.add_paragraph().paragraph_format.space_after = Pt(4)

    # Source Code Sub-Heading
    p_code_hdr = doc.add_paragraph()
    p_code_hdr.paragraph_format.space_before = Pt(4)
    p_code_hdr.paragraph_format.space_after = Pt(2)
    r_c_hdr = p_code_hdr.add_run(f"Java Source Code:")
    r_c_hdr.font.name = "Calibri"
    r_c_hdr.font.bold = True
    r_c_hdr.font.size = Pt(10.5)
    r_c_hdr.font.color.rgb = RGBColor(40, 40, 40)

    for filename, code_text in code_blocks:
        if len(code_blocks) > 1:
            p_fname = doc.add_paragraph()
            p_fname.paragraph_format.space_before = Pt(2)
            p_fname.paragraph_format.space_after = Pt(1)
            r_fn = p_fname.add_run(f"File: {filename}")
            r_fn.font.name = "Consolas"
            r_fn.font.bold = True
            r_fn.font.size = Pt(9.5)
            r_fn.font.color.rgb = RGBColor(0, 102, 153)

        tbl_code = doc.add_table(rows=1, cols=1)
        tbl_code.autofit = False
        tbl_code.columns[0].width = Inches(7.0)
        cell_code = tbl_code.cell(0, 0)
        set_cell_background(cell_code, "F6F8FA") # GitHub code snippet background
        set_cell_border_box(cell_code, "D0D7DE")

        p_code = cell_code.paragraphs[0]
        p_code.paragraph_format.space_before = Pt(4)
        p_code.paragraph_format.space_after = Pt(4)
        p_code.paragraph_format.line_spacing = Pt(12.5)

        r_code = p_code.add_run(code_text.strip())
        r_code.font.name = "Consolas"
        r_code.font.size = Pt(8.5)
        r_code.font.color.rgb = RGBColor(36, 41, 46)

        doc.add_paragraph().paragraph_format.space_after = Pt(4)

    # Execution Output Screenshot
    p_ss_hdr = doc.add_paragraph()
    p_ss_hdr.paragraph_format.space_before = Pt(6)
    p_ss_hdr.paragraph_format.space_after = Pt(4)
    r_ss_hdr = p_ss_hdr.add_run("Execution Output Screenshot:")
    r_ss_hdr.font.name = "Calibri"
    r_ss_hdr.font.bold = True
    r_ss_hdr.font.size = Pt(10.5)
    r_ss_hdr.font.color.rgb = RGBColor(40, 40, 40)

    if os.path.exists(screenshot_path):
        p_img = doc.add_paragraph()
        p_img.alignment = WD_ALIGN_PARAGRAPH.CENTER
        p_img.paragraph_format.space_before = Pt(2)
        p_img.paragraph_format.space_after = Pt(2)
        doc.add_picture(screenshot_path, width=Inches(6.8))

        p_cap = doc.add_paragraph()
        p_cap.alignment = WD_ALIGN_PARAGRAPH.CENTER
        p_cap.paragraph_format.space_after = Pt(12)
        r_cap = p_cap.add_run(f"Figure {prob_num}: Console execution output for Problem {prob_num} (Student ID: {STUDENT_ID})")
        r_cap.font.name = "Calibri"
        r_cap.font.size = Pt(8.5)
        r_cap.font.italic = True
        r_cap.font.color.rgb = RGBColor(100, 100, 100)

print("DOCX and Screenshot generator initialized successfully.")
