package nlp;

// https://github.com/alimpfard/nlp-lex/blob/master/examples/test.nlex
public class NLexFarsiNormalizer extends BaseFarsiNormalizer {
    public NLexFarsiNormalizer() {
        put('٪', '%');
        put('،', ',');
        put('٫', '.');
        put('٬', ',');
        put('٭', '*');
        put("ٯڨڧ", 'ﻕ');
        put("ٱٳٵٲ", 'ﺍ');
        put("ۊۉۋۆۮۈۅۇۄٶۏٷ", 'ﻭ');
        put("ٹٿ", 'ﺙ');
        put("ټٺٽ", 'ﺕ');
        put("ڀﭖ", 'ﭖ');
        put("ځ", 'ﺡ');
        put("څڂڿﺥ", 'خ');
        put("۾ﭺڃ", 'ﭺ');
        put("ڄڇ", 'ﺝ');
        put("ڈډڍڊ", 'ﺩ');
        put("ڌڎڋڏڐ", 'ﺫ');
        put("ڙڒﮊڑ", 'ﮊ');
        put("ږڕڔړ", 'ﺭ');
        put("ۯڗ", 'ﺯ');
        put("ښڛ", 'ﺱ');
        put("ۺڜﺵ", 'ش');
        put("ڝۻ", 'ﺹ');
        put("ڞ", 'ﺽ');
        put("ڟ", 'ﻁ');
        put("ڠۼ", 'ﻍ');
        put("ڣڦڡڢڥڤ", 'ﻑ');
        put("ػګؼڮڭڪڬﮎ", 'ک');
        put("ڴڰڲﮒڱڳ", 'ﮒ');
        put("ڸڷڶڵ", 'ﻝ');
        put("ڹڻںڼڽ", 'ﻥ');
        put("ەۂہۀۃھۿﻩ", 'ه');
        put("۽", 'ﻉ');
        put("ۜ۩ۣۡۖ۬ۢ۞۠ۚۥۤۛ۝ۧۘۦۭ۪ۗ۫ۨ۟ۙ", '_');
        put("یۍېےۓێؽﻱؠۑؾؿٸﻯﺉﯼي", 'ی');
        put("ـ", '_');
    }
}
