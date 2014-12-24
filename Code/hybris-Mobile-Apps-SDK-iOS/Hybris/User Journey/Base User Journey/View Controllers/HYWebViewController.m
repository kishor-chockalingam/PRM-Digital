//
//  HYWebViewController.m
//  Hybris
//
//  Created by Accenture on 14-11-12.
//  Copyright (c) 2014年 Red Ant. All rights reserved.
//

#import "HYWebViewController.h"
#import "Cart.h"
#import "CartEntry.h"

//#define CACHE_CART_ON 

@interface HYWebViewController ()

@property (nonatomic, strong) Cart *cartObj;

@end

@implementation HYWebViewController

#ifdef CACHE_CART_ON
static NSString *JSADDHandler;

+ (void)initialize {
    JSADDHandler = [NSString stringWithContentsOfURL:[[NSBundle mainBundle] URLForResource:@"ajax_handler" withExtension:@"js"] encoding:NSUTF8StringEncoding error:nil];
}
#endif

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.title = @"Product";
    
    [self loadWebPageWithString:self.urlString];
}

- (void)setTitle:(NSString *)title {
    [super setTitle:title];
    UILabel *titleView = (UILabel *)self.navigationItem.titleView;
    
    if (!titleView) {
        titleView = [[ViewFactory shared] make:[HYLabel class]];
        titleView.backgroundColor = [UIColor clearColor];
        titleView.shadowColor = [UIColor colorWithWhite:0.0 alpha:0.5];
        titleView.font = UIFont_navigationBarFont;
        titleView.textColor = UIColor_inverseTextColor;
        self.navigationItem.titleView = titleView;
    }
    
    titleView.text = title;
    [titleView sizeToFit];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)loadWebPageWithString:(NSString *)urlString
{
    NSURLRequest *request =[NSURLRequest requestWithURL:[NSURL URLWithString:urlString]];
    [self.qrWebView loadRequest:request];
}

#ifdef CACHE_CART_ON

- (void)postAddCartWithCode:(NSString *)code quantity:(NSString *)quantity
{
    NSDictionary *userInfo = [[NSDictionary alloc] initWithObjectsAndKeys:code, @"code",quantity , @"quantity", nil];
    [[NSNotificationCenter defaultCenter] postNotificationName:HYItemAddCart object:self userInfo:userInfo];
    
}

- (void)postUpdateCartWithCode:(NSString *)code quantity:(NSString *)quantity
{
    NSDictionary *userInfo = [[NSDictionary alloc] initWithObjectsAndKeys:code, @"code",quantity , @"quantity", nil];
    [[NSNotificationCenter defaultCenter] postNotificationName:HYItemUpdateCart object:self userInfo:userInfo];

}

#endif

#pragma --mark UIWebView Delegate
- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType
{
#ifdef CACHE_CART_ON
    
    NSString *productCode = nil;
    NSString *quantity = nil;
    
    NSString *requestString = [request.URL absoluteString];
    
    NSArray *components = [requestString componentsSeparatedByString:@":"];
    if ([components count] > 2 && [(NSString *)[components objectAtIndex:0] isEqualToString:@"addtocartapp"])
    {
        productCode = [components objectAtIndex:1];
        quantity = [components objectAtIndex:2];
        if ((nil == productCode) || (nil == quantity))
        {
            return NO;
        }
        [self postAddCartWithCode:[components objectAtIndex:1] quantity:[components objectAtIndex:2]];
        
        return NO;
    }

    if ([requestString hasSuffix:@"cart/update"])
    {
        if (nil != request.HTTPBody)
        {
            NSString *body = [[NSString alloc] initWithData:request.HTTPBody encoding:NSUTF8StringEncoding];
            components = [body componentsSeparatedByString:@"&"];
            for (NSString *str in components)
            {
                if ([str hasPrefix:@"productCode="])
                {
                    productCode = [str substringFromIndex:12];
                }
                
                if ([str hasPrefix:@"quantity="])
                {
                    quantity = [str substringFromIndex:9];;
                }
            }
            
            if ((nil == productCode) || (nil == quantity))
            {
                return YES;
            }
            
            [self postUpdateCartWithCode:productCode quantity:quantity];
        }
    }
#endif

    return YES;
}

- (void)webViewDidStartLoad:(UIWebView *)webView
{
    [self waitViewShow:YES];
#ifdef CACHE_CART_ON
    [webView stringByEvaluatingJavaScriptFromString:JSADDHandler];
#endif
}

- (void)webViewDidFinishLoad:(UIWebView *)webView
{
    [self waitViewShow:NO];
}

- (void)webView:(UIWebView *)webView didFailLoadWithError:(NSError *)error
{
    [self waitViewShow:NO];
    
    UIAlertView *alterview = [[UIAlertView alloc] initWithTitle:@"" message:[error localizedDescription] delegate:self cancelButtonTitle:nil otherButtonTitles:@"OK", nil];
    [alterview show];
}

#pragma --mark UIAlertView Delegate
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
//    [self.navigationController popViewControllerAnimated:YES];
}

@end
